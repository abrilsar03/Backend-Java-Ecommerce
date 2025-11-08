INSERT INTO currencies (id, code, name, minor_unit)
VALUES
  (uuid_generate_v4(), 'USD', 'US Dollar', 2),
  (uuid_generate_v4(), 'VES', 'Venezuelan Bolívar', 2),
  (uuid_generate_v4(), 'COP', 'Colombian Peso', 2),
  (uuid_generate_v4(), 'ARS', 'Argentine Peso', 2)
ON CONFLICT (code) DO NOTHING;


WITH
  usd_currency AS (SELECT id FROM currencies WHERE code = 'USD'),
  ves_currency AS (SELECT id FROM currencies WHERE code = 'VES'),
  cop_currency AS (SELECT id FROM currencies WHERE code = 'COP'),
  ars_currency AS (SELECT id FROM currencies WHERE code = 'ARS')
INSERT INTO countries (id, name, iso2, iso3, default_currency_id)
VALUES
  (uuid_generate_v4(), 'Venezuela',     'VE', 'VEN', (SELECT id FROM ves_currency)),
  (uuid_generate_v4(), 'Colombia',      'CO', 'COL', (SELECT id FROM cop_currency)),
  (uuid_generate_v4(), 'Argentina',     'AR', 'ARG', (SELECT id FROM ars_currency)),
  (uuid_generate_v4(), 'United States', 'US', 'USA', (SELECT id FROM usd_currency))
ON CONFLICT (name) DO NOTHING;


INSERT INTO country_currencies (country_id, currency_id)
SELECT country.id, currency.id
FROM countries AS country
JOIN currencies AS currency ON (
  (country.iso2 = 'VE' AND currency.code IN ('VES','USD')) OR
  (country.iso2 = 'CO' AND currency.code IN ('COP','USD')) OR
  (country.iso2 = 'AR' AND currency.code IN ('ARS','USD')) OR
  (country.iso2 = 'US' AND currency.code IN ('USD'))
)
ON CONFLICT DO NOTHING;


INSERT INTO exchange_rates (
  id, base_currency_id, quote_currency_id, rate, source, country_id
)
SELECT
  uuid_generate_v4(),
  base_currency.id,
  quote_currency.id,
  seed_exchange_rates.rate_value,
  seed_exchange_rates.rate_source,
  country.id
FROM (
  VALUES
    ('USD','VES',  40.00000000, 'OFFICIAL', 'VE'),
    ('USD','COP', 4200.00000000, 'OFFICIAL', 'CO'),
    ('USD','ARS',  950.00000000, 'OFFICIAL', 'AR'),
    ('USD','VES',  42.50000000, 'MARKET',   'VE'),
    ('USD','COP', 4300.00000000, 'MARKET',   'CO'),
    ('USD','ARS',  980.00000000, 'MARKET',   'AR')
) AS seed_exchange_rates(base_code, quote_code, rate_value, rate_source, country_iso2)
JOIN currencies AS base_currency  ON base_currency.code  = seed_exchange_rates.base_code
JOIN currencies AS quote_currency ON quote_currency.code = seed_exchange_rates.quote_code
JOIN countries  AS country        ON country.iso2        = seed_exchange_rates.country_iso2
ON CONFLICT DO NOTHING;
