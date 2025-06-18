--liquibase formatted sql

--changeset testuser:insert-customers context:test
INSERT INTO customers (name, email, address) VALUES ('Alice', 'alice@example.com', '123 Main St');
INSERT INTO customers (name, email, address) VALUES ('Bob', 'bob@example.com', '456 Oak Ave');

--changeset testuser:insert-products context:test
INSERT INTO products (name, description, price) VALUES ('Widget', 'A useful widget', 9.99);
INSERT INTO products (name, description, price) VALUES ('Gadget', 'A fancy gadget', 19.99);

--changeset testuser:insert-orders context:test
INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (1, 'SHIPPED', '2024-05-01');
INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (2, 'PROCESSING', '2024-05-02');

--changeset testuser:insert-order-items context:test
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (1, 1, 2, 19.98);
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (2, 2, 1, 19.99);

--changeset testuser:insert-payments context:test
INSERT INTO payments (order_id, payment_date, amount, payment_method) VALUES (1, '2024-05-01 10:00:00', 19.98, 'CREDIT_CARD');
INSERT INTO payments (order_id, payment_date, amount, payment_method) VALUES (2, '2024-05-02 12:00:00', 19.99, 'PAYPAL');

--changeset testuser:insert-inventory context:test
INSERT INTO inventory (product_id, stock_level) VALUES (1, 100);
INSERT INTO inventory (product_id, stock_level) VALUES (2, 50);
