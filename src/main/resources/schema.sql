create schema if not exists candyShop;

CREATE TABLE IF NOT EXISTS baskets
(
    basket_id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users
(
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    enabled BOOLEAN,
    basket_id BIGINT REFERENCES baskets (basket_id),
    role VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS products
(
    product_id  SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL ,
    description VARCHAR(255) NOT NULL ,
    price       DECIMAL(10, 2) NOT NULL ,
    balance     VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS images
(
    image_id   SERIAL PRIMARY KEY,
    name       VARCHAR(255) UNIQUE NOT NULL,
    type       VARCHAR(255) NOT NULL ,
    link       VARCHAR(255) NOT NULL ,
    product_id BIGINT REFERENCES products (product_id)
    );

CREATE TABLE IF NOT EXISTS orders
(
    order_id    SERIAL PRIMARY KEY,
    date        DATE NOT NULL ,
    address     VARCHAR(255) NOT NULL ,
    priceResult DECIMAL(10, 2) NOT NULL ,
    status      VARCHAR(255) NOT NULL,
    user_id INT REFERENCES users (user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS order_product
(
    order_id   BIGINT REFERENCES orders (order_id) ON DELETE CASCADE ON UPDATE NO ACTION ,
    product_id BIGINT REFERENCES products (product_id) ON DELETE CASCADE ON UPDATE NO ACTION ,
    PRIMARY KEY (order_id, product_id)
    );

CREATE TABLE IF NOT EXISTS basket_product
(
    basket_id  BIGINT REFERENCES baskets (basket_id)  ON DELETE CASCADE ON UPDATE NO ACTION ,
    product_id BIGINT REFERENCES products (product_id) ON DELETE CASCADE ON UPDATE NO ACTION ,
    PRIMARY KEY (basket_id, product_id)
    )