-- V31__system_params_seed.sql
INSERT INTO system_params (key, value)
VALUES
  ('min_stock_visibility', '5'),
  ('prob_payment_reject',  '0.07'),
  ('payment_retry_max',    '3'),
ON CONFLICT (key) DO UPDATE
SET value = EXCLUDED.value, updated_at = now();
