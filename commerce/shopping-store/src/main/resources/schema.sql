CREATE SCHEMA IF NOT EXISTS shopping_store;
SET SCHEMA 'shopping_store';

CREATE TABLE IF NOT EXISTS products (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    product_name VARCHAR(255),
    description TEXT,
    image_src VARCHAR(255),
    quantity_state VARCHAR(64),
    product_state VARCHAR(64),
    product_category varchar(64),
    price DECIMAL
);