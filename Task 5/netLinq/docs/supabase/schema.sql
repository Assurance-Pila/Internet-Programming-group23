-- NetLinq Supabase schema (PostgreSQL)
-- Run once in Supabase SQL Editor on a new project.
-- For existing projects, run docs/supabase/migration_v1_linking.sql instead.

-- ---------------------------------------------------------------------------
-- Core tables
-- ---------------------------------------------------------------------------

CREATE TABLE network_metrics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_hash TEXT NOT NULL,
    client_metric_id BIGINT NOT NULL,
    signal_strength INT,
    signal_quality INT,
    network_type TEXT NOT NULL,
    latency_ms INT,
    device_model TEXT,
    android_version TEXT,
    recorded_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (device_hash, client_metric_id)
);

CREATE TABLE qoe_feedback (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_hash TEXT NOT NULL,
    overall_rating SMALLINT NOT NULL CHECK (overall_rating BETWEEN 1 AND 5),
    responsiveness_rating SMALLINT NOT NULL CHECK (responsiveness_rating BETWEEN 1 AND 5),
    streaming_rating SMALLINT NOT NULL CHECK (streaming_rating BETWEEN 1 AND 5),
    call_quality_rating SMALLINT NOT NULL CHECK (call_quality_rating BETWEEN 1 AND 5),
    satisfaction_rating SMALLINT NOT NULL CHECK (satisfaction_rating BETWEEN 1 AND 5),
    trigger_event TEXT,
    network_type TEXT,
    network_metric_id BIGINT,
    metric_recorded_at TIMESTAMPTZ,
    signal_strength_snapshot INT,
    latency_ms_snapshot INT,
    notes TEXT,
    recorded_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ---------------------------------------------------------------------------
-- Indexes
-- ---------------------------------------------------------------------------

CREATE INDEX idx_network_metrics_recorded_at ON network_metrics (recorded_at);
CREATE INDEX idx_network_metrics_network_type ON network_metrics (network_type);
CREATE INDEX idx_network_metrics_device_hash ON network_metrics (device_hash);

CREATE INDEX idx_qoe_feedback_recorded_at ON qoe_feedback (recorded_at);
CREATE INDEX idx_qoe_feedback_device_hash ON qoe_feedback (device_hash);
CREATE INDEX idx_qoe_feedback_network_metric_id ON qoe_feedback (device_hash, network_metric_id);

-- ---------------------------------------------------------------------------
-- Row Level Security — anonymous mobile app inserts only
-- ---------------------------------------------------------------------------

ALTER TABLE network_metrics ENABLE ROW LEVEL SECURITY;
ALTER TABLE qoe_feedback ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow anonymous insert on network_metrics"
    ON network_metrics FOR INSERT
    TO anon
    WITH CHECK (true);

CREATE POLICY "Allow anonymous insert on qoe_feedback"
    ON qoe_feedback FOR INSERT
    TO anon
    WITH CHECK (true);

-- ---------------------------------------------------------------------------
-- Operator analytics views (FR11 — consumed by future web dashboard)
-- ---------------------------------------------------------------------------

CREATE OR REPLACE VIEW operator_qoe_summary AS
SELECT
    date_trunc('hour', recorded_at) AS hour,
    network_type,
    AVG(overall_rating)::NUMERIC(3, 2) AS avg_overall,
    COUNT(*) AS feedback_count
FROM qoe_feedback
GROUP BY 1, 2;

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
