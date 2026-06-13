# Supabase Setup Guide for NetLinq

Follow these steps when creating your Supabase backend. Share the **Project URL** and **anon key** with the team (add to `local.properties`).

For the full entity relationship model, see **[ER_DIAGRAM.md](./ER_DIAGRAM.md)**.

---

## 1. Create the project

1. Go to [supabase.com/dashboard](https://supabase.com/dashboard)
2. **New project** → choose a name (e.g. `netlinq`), region, and database password
3. Wait for provisioning (~2 minutes)

---

## 2. Get API credentials

**Settings → API**

| Key | Where to use |
|-----|--------------|
| **Project URL** | `supabase.url` in `local.properties` |
| **anon public** | `supabase.anon.key` in `local.properties` |

Never commit `local.properties`. The anon key is safe in the app **only when Row Level Security (RLS) is enabled**.

---

## 3. Run the database schema

### New project

In **SQL Editor → New query**, paste and run the full script:

```
docs/supabase/schema.sql
```

This creates:

- `network_metrics` — automatic readings (`client_metric_id` = local Room row id)
- `qoe_feedback` — star ratings with optional link to a reading
- Indexes, RLS policies (anonymous insert only)
- Operator analytics views (`operator_qoe_summary`, `operator_network_quality`, `operator_feedback_with_context`)

### Existing project (original schema without linking columns)

Run instead:

```
docs/supabase/migration_v1_linking.sql
```

---

## 4. Add credentials to the app

```bash
cd netLinq
cp local.properties.example local.properties
```

Edit `local.properties`:

```properties
sdk.dir=/Users/user/Library/Android/sdk
supabase.url=https://YOUR_PROJECT_REF.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIs...
```

Rebuild the app so `BuildConfig` picks up the values:

```bash
./gradlew assembleDebug
```

---

## 5. Verify sync from the app

1. Complete onboarding on device/emulator
2. Open **Dashboard** → tap **Check now** (creates a local reading)
3. Submit feedback (Feedback tab or quick prompt)
4. Tap **Sync** — should report uploaded records
5. In Supabase **Table Editor**, confirm rows in `network_metrics` and `qoe_feedback`
6. Check **SQL Editor**: `SELECT * FROM operator_qoe_summary LIMIT 10;`

---

## 6. What to share with the team

When your Supabase project is ready, share:

1. Project URL
2. Anon public key
3. Confirm `schema.sql` (or migration) was run

Do **not** share the `service_role` key — it bypasses RLS.

---

## 7. App integration (already implemented)

| Component | Location |
|-----------|----------|
| REST client | `data/remote/SupabaseApi.kt` |
| Sync logic | `data/repository/SyncRepository.kt` |
| Background sync | `sync/SyncWorker.kt`, `sync/SyncScheduler.kt` |
| Startup bootstrap | `AppInitializer.kt` (schedules WorkManager after onboarding) |
| WiFi-only setting | Settings → reschedules sync when toggled |

Sync runs every 15 minutes when network constraints are met (WiFi-only by default, user can allow mobile data).
