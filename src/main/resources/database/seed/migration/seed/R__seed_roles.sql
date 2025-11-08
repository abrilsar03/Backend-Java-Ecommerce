INSERT INTO roles (id, code, name) VALUES
  (uuid_generate_v4(), 'ADMIN',  'Administrator'),
  (uuid_generate_v4(), 'CLIENT', 'Client')
ON CONFLICT (code) DO NOTHING