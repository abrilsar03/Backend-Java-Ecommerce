INSERT INTO products (id, title, sku, description, price_cents, photo_url, tax)
SELECT uuid_generate_v4(), product.title, product.sku, product.description, product.price_cents, product.photo_url, product.tax::NUMERIC
FROM (
  VALUES
    ('iPhone 15',          'IP15-128-BLK', 'Apple iPhone 15 128GB Black',     89900, NULL, 0.20),
    ('Galaxy S24',         'S24-256-GRY',  'Samsung Galaxy S24 256GB',         79900, NULL, 0.20),
    ('Dell XPS 13',        'XPS13-16-512', 'Dell XPS 13 16GB/512GB',          129900, NULL, 0.20),
    ('Refurb MacBook Air', 'MBA-REF-13',   'Refurbished MacBook Air 13"',      64900, NULL, 0.20)
) AS product (title, sku, description, price_cents, photo_url, tax)
ON CONFLICT (sku) DO NOTHING;