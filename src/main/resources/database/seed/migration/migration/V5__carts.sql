CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


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
