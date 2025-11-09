CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE products (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  title TEXT NOT NULL,
  sku TEXT NOT NULL UNIQUE,
  description TEXT,
  photo_url TEXT,
  price_cents INT NOT NULL CHECK (price_cents >= 0),
  tax NUMERIC(20,8) NOT NULL DEFAULT 0 CHECK (tax >= 0),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);


CREATE INDEX idx_products_sku ON products(sku);
