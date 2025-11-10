INSERT INTO system_params (key, value)
VALUES
  ('min_stock_visibility', '10'),
  ('prob_payment_reject',  '0.15'),
  ('payment_retry_max',    '3'),
  ('prob_token_reject',    '0.05')
ON CONFLICT (key) DO UPDATE
SET value = EXCLUDED.value, updated_at = now();
