-- NetLinq migration for Supabase projects created with the original schema.sql
-- Adds feedback-linking columns and client_metric_id for cross-table joins.

ALTER TABLE network_metrics
    ADD COLUMN IF NOT EXISTS client_metric_id BIGINT;

-- Backfill existing rows with a synthetic sequence per device (one-time migration)
WITH numbered AS (
    SELECT id, ROW_NUMBER() OVER (PARTITION BY device_hash ORDER BY recorded_at) AS rn
    FROM network_metrics
    WHERE client_metric_id IS NULL
)
UPDATE network_metrics nm
SET client_metric_id = numbered.rn
FROM numbered
WHERE nm.id = numbered.id;

ALTER TABLE network_metrics
    ALTER COLUMN client_metric_id SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS idx_network_metrics_device_client
    ON network_metrics (device_hash, client_metric_id);

ALTER TABLE qoe_feedback
    ADD COLUMN IF NOT EXISTS network_metric_id BIGINT,
    ADD COLUMN IF NOT EXISTS metric_recorded_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS signal_strength_snapshot INT,
    ADD COLUMN IF NOT EXISTS latency_ms_snapshot INT;

CREATE INDEX IF NOT EXISTS idx_qoe_feedback_device_metric
    ON qoe_feedback (device_hash, network_metric_id);

-- Recreate operator views with join support
CREATE OR REPLACE VIEW operator_network_quality AS
SELECT
    date_trunc('hour', recorded_at) AS hour,
    network_type,
    AVG(latency_ms)::NUMERIC(8, 2) AS avg_latency_ms,
    AVG(signal_strength)::NUMERIC(8, 2) AS avg_signal_strength,
    COUNT(*) AS reading_count
FROM network_metrics
WHERE latency_ms IS NOT NULL
GROUP BY 1, 2;

CREATE OR REPLACE VIEW operator_feedback_with_context AS
SELECT
    f.recorded_at,
    f.network_type,
    f.overall_rating,
    f.trigger_event,
    f.signal_strength_snapshot,
    f.latency_ms_snapshot,
    m.latency_ms AS linked_metric_latency_ms,
    m.signal_strength AS linked_metric_signal_strength
FROM qoe_feedback f
LEFT JOIN network_metrics m
    ON f.device_hash = m.device_hash
    AND f.network_metric_id = m.client_metric_id;
