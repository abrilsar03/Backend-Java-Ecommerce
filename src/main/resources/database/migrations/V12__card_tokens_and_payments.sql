CREATE TABLE IF NOT EXISTS card_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    token VARCHAR(255) NOT NULL UNIQUE,
    fingerprint VARCHAR(255) NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    last4 VARCHAR(4) NOT NULL,
    exp_month SMALLINT NOT NULL,
    exp_year SMALLINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_card_tokens_fingerprint ON card_tokens(fingerprint);
CREATE INDEX IF NOT EXISTS idx_card_tokens_token ON card_tokens(token);

CREATE TABLE IF NOT EXISTS tokenization_requests (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    api_key_id UUID REFERENCES api_keys(id),
    fingerprint VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    reason TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idxtokreq_fingerprint ON tokenization_requests(fingerprint);
CREATE INDEX IF NOT EXISTS idxtokreq_status_created ON tokenization_requests(status, created_at);

-- Tabla para pagos
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL UNIQUE REFERENCES orders(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    card_token_id UUID REFERENCES card_tokens(id),
    attempts INT NOT NULL DEFAULT 0,
    reference VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_payments_card_token_id ON payments(card_token_id);

