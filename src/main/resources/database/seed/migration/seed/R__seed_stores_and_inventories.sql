
WITH ve AS (SELECT id FROM countries WHERE iso = 'VEN'),
     co AS (SELECT id FROM countries WHERE iso = 'COL'),
     ar AS (SELECT id FROM countries WHERE iso = 'ARG'),
     us AS (SELECT id FROM countries WHERE iso = 'USA')
INSERT INTO stores (id, name, address, phone_code, phone, country_id)
VALUES
  (uuid_generate_v4(), 'Caracas Main Store',   'Av. Principal, Caracas',  '+58', '2121234567', (SELECT id FROM ve)),
  (uuid_generate_v4(), 'Bogotá Centro',        'Cra 7, Bogotá',           '+57', '1234567890', (SELECT id FROM co)),
  (uuid_generate_v4(), 'Buenos Aires Palermo', 'Av. Scalabrini Ortiz',    '+54', '1144445555', (SELECT id FROM ar)),
  (uuid_generate_v4(), 'New York Downtown',    '5th Avenue, NY',          '+1',  '2125551234', (SELECT id FROM us))
ON CONFLICT DO NOTHING;


INSERT INTO inventories (store_id, product_id, stock, reserved)
SELECT store.id, product.id,
       CASE WHEN product.sku LIKE 'MBA-%' THEN 5
            WHEN product.sku LIKE 'XPS13%' THEN 8
            WHEN product.sku LIKE 'S24-%'  THEN 12
            WHEN product.sku LIKE 'IP15%'  THEN 10
            ELSE 6 END AS stock,
       0 AS reserved
FROM stores AS s
CROSS JOIN products AS p
ON CONFLICT (store_id, product_id) DO NOTHING;
