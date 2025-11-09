INSERT INTO roles (id, role, name) VALUES
  (uuid_generate_v4(), 'ADMIN',  'Administrator'),
  (uuid_generate_v4(), 'CLIENT', 'Client')
ON CONFLICT (role) DO NOTHING