# NetLinq — Backend Implementation Report

**Project:** NetLinq (Internet Programming, Group 23)  
**Document purpose:** Complete backend handoff — architecture, data model, sync, security, and sprint status  
**Aligned to:** SRS.md (Software Requirements Specification)  
**Companion docs:** [ER_DIAGRAM.md](./ER_DIAGRAM.md) · [SUPABASE_SETUP.md](./SUPABASE_SETUP.md)  
**Last updated:** June 2026

---

## 1. Executive summary

NetLinq’s backend is an **offline-first, anonymous data pipeline** for mobile network Quality of Experience (QoE) monitoring in Cameroon.

The system collects two types of data:

1. **Objective readings** — signal strength, network type (2G–5G/WiFi), and latency, gathered automatically on the device.
2. **Subjective feedback** — star ratings from users, optionally linked to the network reading that triggered the prompt.

Data is stored locally first (**Room**), then uploaded to **Supabase (PostgreSQL)** when connectivity is available. No sign-up, login, or personal identifiers are used. Each installation gets a random UUID; only a **SHA-256 hash** (`device_hash`) is sent to the cloud.

The mobile app handles collection, local persistence, and upload. Operator analytics run on **Supabase SQL views** and will be consumed by a future authenticated web dashboard — not the subscriber app.

---

## 2. Backend architecture

NetLinq follows **Clean Architecture** with a dedicated **Data Layer** and supporting **Monitoring** and **Sync** modules.

```
┌─────────────────────────────────────────────────────────────┐
│  Presentation (ViewModels trigger collect / sync actions)   │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│  Domain — NetworkMetric, QoeFeedback, NetworkType, links    │
└───────────────────────────┬─────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────┐
│  Data Layer                                                 │
│  ├── preferences/   DataStore (device ID, settings)         │
│  ├── local/         Room DB (entities, DAOs)                │
│  ├── remote/        Supabase REST API (Retrofit)            │
│  ├── repository/    Repositories (single access point)      │
│  └── mapper/        Entity ↔ domain mappers                 │
└───────────────────────────┬─────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
   Monitoring            Sync              Notifications
   (collectors)      (WorkManager)      (local prompts)
```

**Dependency rule:** Repositories depend on DAOs and API interfaces; domain models have no Android or network dependencies. Hilt wires everything at runtime.

---

## 3. Technology stack

| Component | Technology | Role |
|-----------|------------|------|
| Language | Kotlin 2.0 | Backend logic on Android |
| Local DB | Room (SQLite) v3 | Offline-first storage |
| Preferences | DataStore | Device ID, consent, sync settings |
| Cloud DB | Supabase (PostgreSQL) | Remote persistence + analytics |
| HTTP client | Retrofit + OkHttp | Supabase REST inserts |
| Background sync | WorkManager | Periodic upload (15 min) |
| DI | Hilt (Dagger) | Singleton repositories, workers |
| Serialization | Gson | JSON payloads to Supabase |

**Credentials:** `supabase.url` and `supabase.anon.key` in `local.properties` → compiled into `BuildConfig` at build time. Never committed to git.

---

## 4. Identity and privacy model

### 4.1 Anonymous subscriber model

- On first launch, a random **UUID** is generated and stored in DataStore (`device_id`).
- The raw UUID **never leaves the device**.
- Before upload, `DeviceRepository.getDeviceHash()` computes **SHA-256(UUID)** → `device_hash`.
- All cloud rows are tagged with `device_hash` only.

### 4.2 Implications

| Scenario | Behaviour |
|----------|-----------|
| Same phone, same install | Same anonymous contributor |
| Reinstall or new phone | New UUID → new `device_hash` |
| Personal dashboard | Local Room history on device |
| Operator view | Aggregated trends across many `device_hash` values |

Aligns with SRS FR1 and Section 8.3 (privacy by design).

---

## 5. Local storage

### 5.1 DataStore (`data/preferences/AppPreferences.kt`)

Not synced to cloud. Holds runtime configuration:

| Preference key | Type | Default | Purpose |
|----------------|------|---------|---------|
| `device_id` | String | auto-generated UUID | Local anonymous identity |
| `onboarding_complete` | Boolean | false | Gates sync bootstrap |
| `monitoring_enabled` | Boolean | false → true on onboarding | Background collection |
| `wifi_only_sync` | Boolean | true | WorkManager network constraint |
| `feedback_frequency` | Int | 1 (normal) | Prompt cooldown: 5 / 15 / 30 min |
| `trigger_signal` | Boolean | true | Signal drop prompts |
| `trigger_network` | Boolean | true | Network type change prompts |
| `trigger_latency` | Boolean | true | Latency spike prompts |
| `trigger_connectivity` | Boolean | true | Connection loss prompts |
| `theme_mode` | Int | 0 (system) | UI only — not backend |

### 5.2 Room database (`data/local/`)

**Database name:** `netlinq.db`  
**Version:** 3  
**Migrations:** 1→2 (feedback linking columns), 2→3 (FK + index on `networkMetricId`)

#### Table: `network_metrics`

| Column | Type | Description |
|--------|------|-------------|
| `id` | Long PK | Auto-increment |
| `signalStrength` | Int? | dBm from TelephonyManager |
| `signalQuality` | Int? | RSRP / quality indicator |
| `networkType` | String | 2G, 3G, 4G, 5G, WiFi, None |
| `latencyMs` | Int? | HEAD request round-trip time |
| `deviceModel` | String | e.g. Pixel 7 |
| `androidVersion` | String | e.g. 14 |
| `recordedAt` | Long | Epoch milliseconds |
| `synced` | Boolean | false until Supabase upload succeeds |

#### Table: `qoe_feedback`

| Column | Type | Description |
|--------|------|-------------|
| `id` | Long PK | Auto-increment |
| `overallRating` … `satisfactionRating` | Int | 1–5 stars (five categories) |
| `triggerEvent` | String? | e.g. SIGNAL_DEGRADATION |
| `networkType` | String? | Context at rating time |
| `networkMetricId` | Long? FK | → `network_metrics.id`, ON DELETE SET NULL |
| `metricRecordedAt` | Long? | Timestamp of linked reading |
| `signalStrengthSnapshot` | Int? | Copied at rating time |
| `latencyMsSnapshot` | Int? | Copied at rating time |
| `notes` | String? | Optional user comment |
| `recordedAt` | Long | Epoch milliseconds |
| `synced` | Boolean | false until upload succeeds |

**Relationship:** Feedback can optionally reference the exact network reading that triggered it. Snapshots preserve context even after the linked metric is purged locally post-sync.

---

## 6. Cloud storage (Supabase)

### 6.1 Schema

Full SQL: `docs/supabase/schema.sql`  
Migration for older projects: `docs/supabase/migration_v1_linking.sql`

#### Table: `network_metrics`

| Column | Type | Notes |
|--------|------|-------|
| `id` | UUID PK | Server-generated |
| `device_hash` | TEXT | SHA-256 of local UUID |
| `client_metric_id` | BIGINT | Room `id` from device — enables joins |
| `signal_strength`, `signal_quality` | INT | Nullable |
| `network_type` | TEXT | NOT NULL |
| `latency_ms` | INT | Nullable |
| `device_model`, `android_version` | TEXT | Device metadata |
| `recorded_at` | TIMESTAMPTZ | From device |
| `created_at` | TIMESTAMPTZ | Server default |

**Unique constraint:** `(device_hash, client_metric_id)` — one cloud row per local metric per device.

#### Table: `qoe_feedback`

Same five rating columns as Room, plus linking fields (`network_metric_id`, snapshots, `metric_recorded_at`). Ratings constrained to 1–5 via CHECK constraints.

#### Cloud join rule

```sql
qoe_feedback.network_metric_id = network_metrics.client_metric_id
AND qoe_feedback.device_hash = network_metrics.device_hash
```

Cloud UUIDs are server-side; client-side Room IDs are carried as `client_metric_id` / `network_metric_id` for correlation within the same anonymous device.

### 6.2 Indexes

- `recorded_at`, `network_type`, `device_hash` on both tables
- Composite `(device_hash, network_metric_id)` on feedback for join performance

### 6.3 Row Level Security

| Policy | Role | Permission |
|--------|------|------------|
| Allow anonymous insert on `network_metrics` | `anon` | INSERT only |
| Allow anonymous insert on `qoe_feedback` | `anon` | INSERT only |

Mobile app cannot read other users’ data. Operator access will use authenticated roles on a future web dashboard (service role / custom JWT — not in mobile app).

### 6.4 Operator analytics views (FR11)

| View | Output |
|------|--------|
| `operator_qoe_summary` | Hourly avg overall rating by network type |
| `operator_network_quality` | Hourly avg latency and signal by network type |
| `operator_feedback_with_context` | Feedback LEFT JOIN linked metric reading |

Not exposed in the subscriber mobile UI.

---

## 7. Data collection pipeline

### 7.1 Components (`monitoring/`)

| Class | Responsibility |
|-------|----------------|
| `NetworkMonitorService` | Orchestrates one reading: type + signal + latency → save to Room |
| `NetworkTypeDetector` | 2G/3G/4G/5G/WiFi via ConnectivityManager + TelephonyManager |
| `SignalStrengthCollector` | dBm from registered cell (LTE, NR, GSM, WCDMA, CDMA) |
| `LatencyMeasurer` | HEAD request to `google.com/generate_204`, returns ms or null |
| `NetworkDegradationDetector` | Compares consecutive readings → trigger event |
| `NetworkMonitoringManager` | 60s poll + ConnectivityManager callbacks; emits prompt events |

### 7.2 Collection triggers

| Trigger | Condition |
|---------|-----------|
| Periodic poll | Every 60 seconds when monitoring enabled |
| Manual | Dashboard “Check now” button |
| Network callback | Connectivity or capability change |

### 7.3 Degradation detection thresholds

| Event | Rule |
|-------|------|
| Signal degradation | Current dBm ≤ previous − 10 |
| Network type change | e.g. 4G → 3G (both non-None) |
| Latency spike | Current ≥ previous + 200 ms AND ≥ 2× previous |
| Connectivity interruption | Online ↔ offline transition |

User can disable each trigger in Settings. Prompt cooldown: 5 / 15 / 30 minutes based on feedback frequency setting.

### 7.4 Permissions required

| Permission | Used for |
|------------|----------|
| READ_PHONE_STATE | Signal strength, network type |
| ACCESS_FINE_LOCATION | Cell info on Android 10+ |
| INTERNET | Latency measurement, sync |
| POST_NOTIFICATIONS | Background feedback prompts |

---

## 8. Synchronization pipeline

### 8.1 Flow

```
Room (synced = false)
    → SyncRepository.syncAll()
        → DeviceRepository.getDeviceHash()
        → Map domain → JSON payload
        → SupabaseApi POST (batch insert)
        → On 2xx: markSynced(ids)
        → SyncWorker also calls purgeOldRecords(7 days)
```

### 8.2 Sync triggers

| Trigger | When |
|---------|------|
| WorkManager periodic | Every 15 minutes (unique work `netlinq_sync`) |
| App cold start | `AppInitializer` reschedules if onboarding complete |
| Onboarding complete | `OnboardingViewModel` schedules initial sync |
| Settings change | WiFi-only toggle reschedules WorkManager constraints |
| Manual | Dashboard “Sync” button |

### 8.3 Network constraints

- **Default:** WiFi only (`NetworkType.UNMETERED`)
- **User override:** Settings → allow sync on mobile data (`NetworkType.CONNECTED`)

### 8.4 Retention (FR12)

After successful sync, local rows older than **7 days** are deleted. Cloud data remains for operator analytics.

### 8.5 API endpoints

| Method | Path | Body |
|--------|------|------|
| POST | `/rest/v1/network_metrics` | `List<NetworkMetricPayload>` |
| POST | `/rest/v1/qoe_feedback` | `List<QoeFeedbackPayload>` |

Headers: `apikey`, `Authorization: Bearer <anon_key>`, `Content-Type: application/json`, `Prefer: return=minimal`.

### 8.6 Failure handling

- Supabase not configured → sync returns failure (“Supabase not configured”)
- HTTP error on metrics batch → entire sync aborts; nothing marked synced
- HTTP error on feedback batch → metrics may already be synced; feedback retried next run
- WorkManager → `Result.retry()` on failure

---

## 9. Repository layer

| Repository | Methods | Backing store |
|------------|---------|---------------|
| `DeviceRepository` | `getOrCreateDeviceId()`, `getDeviceHash()` | DataStore |
| `NetworkMetricRepository` | `save`, `observeMetrics`, `getUnsynced`, `markSynced`, `purgeSyncedOlderThan` | Room |
| `QoeFeedbackRepository` | Same pattern as metrics | Room |
| `SyncRepository` | `syncAll()`, `purgeOldRecords()`, `isConfigured()` | Room + Supabase |

All repositories are `@Singleton` and injected via Hilt.

---

## 10. Folder structure (backend)

```
app/src/main/java/com/netlinq/
├── AppInitializer.kt              Sync bootstrap on cold start
├── NetLinqApplication.kt          Hilt + WorkManager factory
│
├── domain/model/
│   ├── NetworkMetric.kt
│   ├── QoeFeedback.kt
│   ├── NetworkType.kt
│   └── FeedbackNetworkLink.kt
│
├── data/
│   ├── preferences/AppPreferences.kt
│   ├── local/
│   │   ├── NetLinqDatabase.kt     Room v3
│   │   ├── entity/                NetworkMetricEntity, QoeFeedbackEntity
│   │   └── dao/                   NetworkMetricDao, QoeFeedbackDao
│   ├── remote/SupabaseApi.kt      Retrofit interface + payloads
│   ├── repository/                Device, NetworkMetric, QoeFeedback, Sync
│   └── mapper/EntityMappers.kt
│
├── monitoring/
│   ├── NetworkMonitorService.kt
│   ├── NetworkMonitoringManager.kt
│   ├── NetworkDegradationDetector.kt
│   ├── SignalStrengthCollector.kt
│   ├── LatencyMeasurer.kt
│   └── NetworkTypeDetector.kt
│
├── sync/
│   ├── SyncWorker.kt              WorkManager CoroutineWorker
│   └── SyncScheduler.kt           Periodic work enqueue
│
├── notifications/
│   └── FeedbackNotificationHelper.kt   Local notifications (not FCM)
│
└── di/
    ├── DatabaseModule.kt          Room + migrations
    └── NetworkModule.kt           OkHttp + Retrofit + SupabaseApi

docs/
├── BACKEND_REPORT.md              This document
├── ER_DIAGRAM.md                  ER diagrams + presentation notes
├── SUPABASE_SETUP.md              Cloud setup guide
└── supabase/
    ├── schema.sql                 Full PostgreSQL schema
    └── migration_v1_linking.sql   Upgrade script
```

---

## 11. SRS functional requirements — backend status

| ID | Requirement | Status |
|----|-------------|--------|
| FR1 | Anonymous device identifier | **DONE** — UUID + SHA-256 hash |
| FR2 | Subjective QoE ratings (5 categories) | **DONE** — Room + Supabase |
| FR3 | Event-triggered feedback prompts | **DONE** — detector + notifications |
| FR4 | Signal strength collection | **DONE** — TelephonyManager |
| FR5 | Network type detection | **DONE** — 2G–5G/WiFi |
| FR6 | Periodic latency measurement | **PARTIAL** — 60s poll + manual; not full background service |
| FR7 | Local Room storage | **DONE** — v3 with FK |
| FR8 | Offline operation | **DONE** |
| FR9 | Supabase sync | **DONE** — manual + WorkManager |
| FR10 | Personal QoE dashboard | **DONE** — reads local Room |
| FR11 | Operator aggregated reports | **PARTIAL** — SQL views live; web dashboard future |
| FR12 | Purge synced records after 7 days | **DONE** — SyncWorker |

---

## 12. Security summary

| Concern | Mitigation |
|---------|------------|
| Personal identity | No name, email, phone; UUID hashed before upload |
| Transport | HTTPS only (Retrofit/OkHttp) |
| Cloud access | RLS: anon INSERT only on both tables |
| Credentials | `local.properties` gitignored; anon key safe with RLS |
| Operator data | Aggregated views; no PII in analytics layer |
| Mobile scope | No admin/operator APIs in subscriber app |

---

## 13. Verification checklist

Run after Supabase schema is deployed and credentials are in `local.properties`:

- [ ] `./gradlew assembleDebug` succeeds
- [ ] Complete onboarding → device ID generated in DataStore
- [ ] Dashboard **Check now** → row in Room `network_metrics`
- [ ] Submit feedback → row in Room `qoe_feedback` with optional link
- [ ] **Sync** → status shows uploaded count
- [ ] Supabase Table Editor shows rows in both tables with matching `device_hash`
- [ ] `client_metric_id` on metrics matches `network_metric_id` on linked feedback
- [ ] `SELECT * FROM operator_qoe_summary LIMIT 5` returns data
- [ ] Toggle WiFi-only sync → WorkManager rescheduled (no crash)
- [ ] App restart → sync still scheduled (`AppInitializer`)

---

## 14. Known limitations and future work

| Item | Notes |
|------|-------|
| Operator web dashboard | Views exist in Supabase; authenticated UI not built |
| Admin roles on Supabase | Backend-assigned; not on mobile |
| FCM remote push | Local notifications only today |
| FR6 background service | 60s in-app poll; no dedicated foreground service |
| Automated tests | No unit/integration tests for repositories yet |
| Release signing | Debug APK only for team testing |
| Duplicate uploads | No server-side dedup beyond `(device_hash, client_metric_id)` unique index |

---

## 15. Quick reference for presentations

**One-liner:**  
> NetLinq’s backend is offline-first: Room stores readings and ratings on the phone, WorkManager syncs anonymized batches to Supabase over HTTPS, and SQL views power future operator analytics.

**Three layers to draw on a slide:**
1. **Collect** — TelephonyManager + latency probe → Room  
2. **Store** — Room (local) + DataStore (identity/settings)  
3. **Sync** — Retrofit → Supabase with RLS and `device_hash`

**ER diagram:** See [ER_DIAGRAM.md](./ER_DIAGRAM.md) for Mermaid diagrams and join rules.

---

*End of backend report.*
