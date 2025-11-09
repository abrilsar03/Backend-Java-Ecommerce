CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS event_logs (
  id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  request_id  UUID NOT NULL,
  event_type  TEXT NOT NULL, CHECK (event_type IN ('CREATE', 'UPDATE', 'DELETE')),
  entity_type TEXT,     CHECK (entity_type IN ('USER', 'ORDER', 'PRODUCT', "PAYMENT", "SYSTEM_PARAM")),
  entity_id   UUID,
  level       TEXT NOT NULL, CHECK (level IN ('DEBUG', 'INFO', 'WARN', 'ERROR')),
  payload     JSONB,        
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_event_logs_request_id ON event_logs(request_id);
CREATE INDEX IF NOT EXISTS idx_event_logs_event_created ON event_logs(event_type, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_event_logs_level_created ON event_logs(level, created_at DESC);
