CREATE TABLE IF NOT EXISTS products (
                                        id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS attributes (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS attribute_values (
                                                id SERIAL PRIMARY KEY,
                                                attribute_id INT REFERENCES attributes(id) ON DELETE CASCADE,
    value VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS product_variations (
                                                  id SERIAL PRIMARY KEY,
                                                  product_id INT REFERENCES products(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS product_variation_attributes (
                                                            id SERIAL PRIMARY KEY,
                                                            variation_id INT REFERENCES product_variations(id) ON DELETE CASCADE,
    attribute_value_id INT REFERENCES attribute_values(id) ON DELETE CASCADE,
    UNIQUE(variation_id, attribute_value_id)
    );
