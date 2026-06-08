# Supabase Setup Guide for NetLinq

Follow these steps when creating your Supabase backend. Share the **Project URL** and **anon key** with the team (add to `local.properties`).

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

## 3. Run initial schema (SQL Editor)

Paste and run in **SQL Editor → New query**:

```sql
-- Anonymous device identifier (hashed on device before upload)
CREATE TABLE network_metrics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_hash TEXT NOT NULL,
    signal_strength INT,
    signal_quality INT,
    network_type TEXT NOT NULL,
    latency_ms INT,
    device_model TEXT,
    android_version TEXT,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE qoe_feedback (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_hash TEXT NOT NULL,
    overall_rating SMALLINT CHECK (overall_rating BETWEEN 1 AND 5),
    responsiveness_rating SMALLINT CHECK (responsiveness_rating BETWEEN 1 AND 5),
    streaming_rating SMALLINT CHECK (streaming_rating BETWEEN 1 AND 5),
    call_quality_rating SMALLINT CHECK (call_quality_rating BETWEEN 1 AND 5),
    satisfaction_rating SMALLINT CHECK (satisfaction_rating BETWEEN 1 AND 5),
    trigger_event TEXT,
    network_type TEXT,
    notes TEXT,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Indexes for operator analytics
CREATE INDEX idx_network_metrics_recorded_at ON network_metrics (recorded_at);
CREATE INDEX idx_network_metrics_network_type ON network_metrics (network_type);
CREATE INDEX idx_qoe_feedback_recorded_at ON qoe_feedback (recorded_at);

-- Enable Row Level Security
ALTER TABLE network_metrics ENABLE ROW LEVEL SECURITY;
ALTER TABLE qoe_feedback ENABLE ROW LEVEL SECURITY;

-- Allow anonymous inserts only (no reads of other users' raw data from the app)
CREATE POLICY "Allow anonymous insert on network_metrics"
    ON network_metrics FOR INSERT
    TO anon
    WITH CHECK (true);

CREATE POLICY "Allow anonymous insert on qoe_feedback"
    ON qoe_feedback FOR INSERT
    TO anon
    WITH CHECK (true);

-- Operator analytics view (aggregated, no device_hash exposed in reports)
CREATE OR REPLACE VIEW operator_qoe_summary AS
SELECT
    date_trunc('hour', recorded_at) AS hour,
    network_type,
    AVG(overall_rating)::NUMERIC(3,2) AS avg_overall,
    COUNT(*) AS feedback_count
FROM qoe_feedback
GROUP BY 1, 2;
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

Rebuild the app so `BuildConfig` picks up the values.

---

## 5. What to share with the team

When your Supabase project is ready, share:

1. Project URL
2. Anon public key
3. Confirm the SQL schema above was run

Do **not** share the `service_role` key — it bypasses RLS.

---

## Next app integration steps

Once credentials are in place, we will add:

- Retrofit/Ktor Supabase REST client
- WorkManager sync worker for unsynced Room records
- WiFi-only sync setting (per SRS)
