CREATE TABLE attributes
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE categories
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    parent_id bigint,
    constraint fk_category_parent
        foreign key (parent_id) references categories (id) on delete cascade
);

CREATE TABLE category_attributes
(
    category_id  INT NOT NULL REFERENCES categories (id) ON DELETE CASCADE,
    attribute_id INT NOT NULL REFERENCES attributes (id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, attribute_id)
);

CREATE TABLE products
(
    id          SERIAL PRIMARY KEY,
    category_id INT NOT NULL REFERENCES categories (id),
    created_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE product_variations
(
    id          SERIAL PRIMARY KEY,
    product_id  INT            NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    name        TEXT           NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL
);

CREATE TABLE attribute_values
(
    id           SERIAL PRIMARY KEY,
    variation_id INT  NOT NULL REFERENCES product_variations (id) ON DELETE CASCADE,
    attribute_id INT  NOT NULL REFERENCES attributes (id),
    value        TEXT NOT NULL,
    UNIQUE (variation_id, attribute_id)
);
