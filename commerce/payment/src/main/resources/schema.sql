CREATE SCHEMA IF NOT EXISTS payment_gateway;
SET SCHEMA 'payment_gateway';

CREATE TABLE IF NOT EXISTS payments (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL,
    products_total DECIMAL,
    delivery_total DECIMAL,
    total_payment  DECIMAL,
    fee_total DECIMAL,
    payment_state VARCHAR(50)
);