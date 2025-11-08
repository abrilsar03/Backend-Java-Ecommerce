CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE currencies (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  code CHAR(3) NOT NULL UNIQUE, CHECK (code IN ('USD', 'VES', 'COP', 'ARS')),
  name TEXT NOT NULL,
  minor_unit SMALLINT NOT NULL DEFAULT 2,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);


CREATE TABLE countries (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT NOT NULL UNIQUE,
  iso2 CHAR(2) NOT NULL UNIQUE,
  iso3 CHAR(3) NOT NULL UNIQUE,
  default_currency_id UUID REFERENCES currencies(id),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);


CREATE TABLE country_currencies (
  country_id  UUID NOT NULL REFERENCES countries(id) ON DELETE CASCADE,
  currency_id UUID NOT NULL REFERENCES currencies(id),
  PRIMARY KEY (country_id, currency_id)
);


CREATE TABLE exchange_rates (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  base_currency_id  UUID NOT NULL REFERENCES currencies(id),
  quote_currency_id UUID NOT NULL REFERENCES currencies(id),
  rate NUMERIC(20,8) NOT NULL,              
  valid_from TIMESTAMPTZ NOT NULL DEFAULT now(),
  valid_to   TIMESTAMPTZ,
  source TEXT NOT NULL, CHECK (source IN ('OFFICIAL', 'MARKET')),
  country_id UUID REFERENCES countries(id),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ
);


CREATE INDEX idx_exchange_rates_pair ON exchange_rates(base_currency_id, quote_currency_id);
CREATE INDEX idx_exchange_rates_valid_from ON exchange_rates(valid_from DESC);
CREATE INDEX idx_exchange_rates_country ON exchange_rates(country_id);
