CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  role TEXT NOT NULL UNIQUE, CHECK (role IN ('ADMIN', 'CLIENT')),
  name TEXT NOT NULL
);

CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  first_name TEXT NOT NULL,
  last_name  TEXT NOT NULL,
  email      TEXT NOT NULL UNIQUE,
  password   TEXT NOT NULL,
  phone_code TEXT,
  phone      TEXT,
  documentNumber        TEXT,
  documentType   TEXT CHECK (documentType IN ('V','E','J','DNI','CUIT','SSN')),
  address        TEXT NOT NULL,
  active     BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);


CREATE TABLE user_roles (
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role_id UUID NOT NULL REFERENCES roles(id),
  PRIMARY KEY (user_id, role_id)
);


CREATE INDEX idx_users_created_at ON users(created_at DESC);
