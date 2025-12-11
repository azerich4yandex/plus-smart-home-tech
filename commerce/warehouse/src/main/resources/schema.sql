CREATE SCHEMA IF NOT EXISTS warehouse;
SET SCHEMA 'warehouse';

CREATE TABLE IF NOT EXISTS warehouse_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    quantity BIGINT DEFAULT 0,
    fragile BOOLEAN NOT NULL,
    weight DECIMAL NOT NULL,
    width DECIMAL NOT NULL,
    height DECIMAL NOT NULL,
    depth DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS order_booking (
    order_id UUID PRIMARY KEY,
    delivery_id UUID
);

CREATE TABLE IF NOT EXISTS order_booking_products (
    order_booking_id UUID REFERENCES order_booking(order_id),
    product_id uuid NOT NULL,
    quantity BIGINT NOT NULL DEFAULT 0
);