CREATE SCHEMA IF NOT EXISTS shopping_cart;
SET SCHEMA 'shopping_cart';

CREATE TABLE IF NOT EXISTS shopping_carts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_name VARCHAR(255),
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS shopping_cart_products (
    product_id UUID PRIMARY KEY,
    shopping_cart_id UUID REFERENCES shopping_carts(id),
    quantity BIGINT NOT NULL
);