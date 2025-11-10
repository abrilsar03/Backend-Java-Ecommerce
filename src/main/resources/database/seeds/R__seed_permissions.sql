CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles (id, role, name) VALUES
  (uuid_generate_v4(), 'ADMIN',  'Administrator'),
  (uuid_generate_v4(), 'CLIENT', 'Client')
ON CONFLICT (role) DO NOTHING;

INSERT INTO permissions (resource, action) VALUES
  ('PRODUCT','READ'),
  ('PRODUCT','CREATE'),
  ('PRODUCT','UPDATE'),
  ('PRODUCT','DELETE'),
  ('ORDER','READ'),
  ('ORDER','CREATE'),
  ('ORDER','UPDATE'),
  ('ORDER','DELETE'),
  ('USER','READ'),
  ('USER','CREATE'),
  ('USER','UPDATE'),
  ('USER','DELETE'),
  ('CART','READ'),
  ('CART','CREATE'),
  ('CART','UPDATE'),
  ('CART','DELETE'),
  ('PAYMENT','READ'),
  ('PAYMENT','CREATE'),
  ('PAYMENT','UPDATE'),
  ('PAYMENT','DELETE'),
  ('SYSTEM_PARAM','READ'),
  ('SYSTEM_PARAM','CREATE'),
  ('SYSTEM_PARAM','UPDATE'),
  ('SYSTEM_PARAM','DELETE'),
  ('CARD_TOKEN','READ'),
  ('CARD_TOKEN','CREATE'),
  ('CARD_TOKEN','UPDATE'),
  ('CARD_TOKEN','DELETE'),
  ('API_KEY','READ'),
  ('API_KEY','CREATE'),
  ('API_KEY','UPDATE'),
  ('API_KEY','DELETE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r CROSS JOIN permissions p
WHERE r.role = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON (
  (p.resource='PRODUCT' AND p.action='READ') OR
  (p.resource='USER' AND p.action IN ('READ','UPDATE')) OR
  (p.resource='ORDER' AND p.action IN ('READ','UPDATE')) OR
  (p.resource='CARD_TOKEN' AND p.action IN ('READ','CREATE'))
)
WHERE r.role = 'CLIENT'
ON CONFLICT DO NOTHING;
