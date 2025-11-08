INSERT INTO categories (id, name, description, icon, image_url)
VALUES
  (uuid_generate_v4(), 'Electronics', 'Devices and gadgets', 'ph-device', NULL),
  (uuid_generate_v4(), 'Phones',      'Mobile phones',       'ph-phone',  NULL),
  (uuid_generate_v4(), 'Laptops',     'Notebooks & laptops', 'ph-laptop', NULL),
  (uuid_generate_v4(), 'Home',        'Home appliances',     'ph-home',   NULL)
ON CONFLICT (name) DO NOTHING;

WITH electronics_category AS (
  SELECT id FROM categories WHERE name = 'Electronics'
)
UPDATE categories AS child_category
SET parent_id = (SELECT id FROM electronics_category)
WHERE child_category.name IN ('Phones','Laptops');


INSERT INTO tags (id, name, icon, color)
VALUES
  (uuid_generate_v4(), 'New',       'star',   '#00A3FF'),
  (uuid_generate_v4(), 'Refurb',    'wrench', '#FF9800'),
  (uuid_generate_v4(), 'Clearance', 'sale',   '#E53935')
ON CONFLICT (name) DO NOTHING;

WITH usd_currency AS (
  SELECT id FROM currencies WHERE code = 'USD'
)

INSERT INTO products (
  id, title, sku, description, price_cents, currency_id, tax_class
)
VALUES
  (uuid_generate_v4(), 'iPhone 15',          'IP15-128-BLK', 'Apple iPhone 15 128GB Black',     89900, (SELECT id FROM usd_currency), 'STANDARD'),
  (uuid_generate_v4(), 'Galaxy S24',         'S24-256-GRY',  'Samsung Galaxy S24 256GB',         79900, (SELECT id FROM usd_currency), 'STANDARD'),
  (uuid_generate_v4(), 'Dell XPS 13',        'XPS13-16-512', 'Dell XPS 13 16GB/512GB',          129900, (SELECT id FROM usd_currency), 'STANDARD'),
  (uuid_generate_v4(), 'Refurb MacBook Air', 'MBA-REF-13',   'Refurbished MacBook Air 13"',      64900, (SELECT id FROM usd_currency), 'STANDARD')
ON CONFLICT (sku) DO NOTHING;


INSERT INTO product_categories (product_id, category_id)
SELECT product.id, category.id
FROM products  AS product
JOIN categories AS category
  ON (
    (product.sku LIKE 'IP15%'  AND category.name IN ('Electronics','Phones')) OR
    (product.sku LIKE 'S24-%'  AND category.name IN ('Electronics','Phones')) OR
    (product.sku LIKE 'XPS13%' AND category.name IN ('Electronics','Laptops')) OR
    (product.sku LIKE 'MBA-%'  AND category.name IN ('Electronics','Laptops'))
  )
ON CONFLICT DO NOTHING;


INSERT INTO product_tags (product_id, tag_id)
SELECT product.id, tag.id
FROM products AS product
JOIN tags     AS tag
  ON (
    (product.sku LIKE 'IP15%'  AND tag.name = 'New')    OR
    (product.sku LIKE 'S24-%'  AND tag.name = 'New')    OR
    (product.sku LIKE 'XPS13%' AND tag.name = 'New')    OR
    (product.sku LIKE 'MBA-%'  AND tag.name = 'Refurb')
  )
ON CONFLICT DO NOTHING;


INSERT INTO product_photos (id, product_id, url, position)
SELECT
  uuid_generate_v4(),
  product.id,
  CONCAT('https://s3.example.com/', product.sku, '/1.jpg') AS photo_url,
  0 AS position
FROM products AS product
ON CONFLICT DO NOTHING;
