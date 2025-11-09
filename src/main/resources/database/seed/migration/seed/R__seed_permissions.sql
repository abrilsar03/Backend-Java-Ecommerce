-- Roles
INSERT INTO roles (code, name) VALUES
  ('ADMIN','Administrator'),
  ('CLIENT','Client')
ON CONFLICT (code) DO NOTHING;

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
  ('USER','DELETE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON (
  (p.resource='PRODUCT' AND p.action='READ') OR
  (p.resource='ORDER'   AND p.action IN ('READ','CREATE'))
)
WHERE r.code = 'CLIENT'
ON CONFLICT DO NOTHING;
