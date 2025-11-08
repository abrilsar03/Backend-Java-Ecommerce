CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE stores (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT NOT NULL,
  address TEXT,
  phone_code TEXT,
  phone TEXT,
  country_id UUID NOT NULL REFERENCES countries(id),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);

CREATE INDEX idx_stores_country ON stores(country_id);


CREATE TABLE inventories (
  store_id UUID NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
  product_id UUID NOT NULL REFERENCES products(id),
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  reserved INT NOT NULL DEFAULT 0 CHECK (reserved >= 0),
  PRIMARY KEY (store_id, product_id)
);

CREATE INDEX idx_inventories_product ON inventories(product_id);


CREATE TABLE carts (
  user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  store_id UUID REFERENCES stores(id),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_carts_store ON carts(store_id);


CREATE TABLE cart_items (
  user_id UUID NOT NULL REFERENCES carts(user_id) ON DELETE CASCADE,
  product_id UUID NOT NULL REFERENCES products(id),
  quantity INT NOT NULL CHECK (quantity > 0),
  PRIMARY KEY (user_id, product_id)
);

CREATE INDEX idx_cart_items_product ON cart_items(product_id);
