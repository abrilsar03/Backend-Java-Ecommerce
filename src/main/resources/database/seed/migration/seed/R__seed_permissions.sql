INSERT INTO permissions (id, resource, action) VALUES
  (uuid_generate_v4(), 'PRODUCT', 'READ'),
  (uuid_generate_v4(), 'PRODUCT', 'WRITE'),
  (uuid_generate_v4(), 'ORDER',   'READ'),
  (uuid_generate_v4(), 'ORDER',   'WRITE'),
  (uuid_generate_v4(), 'USER',    'READ'),
  (uuid_generate_v4(), 'USER',    'WRITE')
ON CONFLICT DO NOTHING;


INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p
  ON ( (p.resource='PRODUCT' AND p.action='READ')
    OR (p.resource='ORDER'   AND p.action IN ('READ','WRITE')) )
WHERE r.code = 'CLIENT'
ON CONFLICT DO NOTHING;
