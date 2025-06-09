CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE attributes
(
    id          uuid PRIMARY KEY default gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE categories
(
    id          uuid PRIMARY KEY default gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    route_location  VARCHAR(100) not null,
    parent_id uuid,
    constraint fk_category_parent
        foreign key (parent_id) references categories (id) on delete cascade
);

CREATE TABLE category_attributes
(
    category_id  uuid NOT NULL REFERENCES categories (id) ON DELETE CASCADE,
    attribute_id uuid NOT NULL REFERENCES attributes (id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, attribute_id)
);

CREATE TABLE products
(
    id          uuid PRIMARY KEY default gen_random_uuid(),
    seller_id   uuid NOT NULL,
    category_id uuid NOT NULL REFERENCES categories (id),
    created_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE product_variations
(
    id          uuid PRIMARY KEY default gen_random_uuid(),
    product_id  uuid            NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    name        TEXT           NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE attribute_values
(
    variation_id uuid  NOT NULL REFERENCES product_variations (id) ON DELETE CASCADE,
    attribute_id uuid  NOT NULL REFERENCES attributes (id),
    primary key (variation_id, attribute_id),
    value        TEXT NOT NULL
);
