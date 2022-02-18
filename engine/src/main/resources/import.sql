INSERT INTO ORDERS (created_at, updated_at) VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
SELECT @order_id := SCOPE_IDENTITY()
INSERT INTO ORDER_ITEMS (order_id, name, quantity, price, created_at, updated_at) VALUES (@order_id, 'name #1', 1, 10.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ORDER_ITEMS (order_id, name, quantity, price, created_at, updated_at) VALUES (@order_id, 'name #2', 2, 20.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ORDER_ITEMS (order_id, name, quantity, price, created_at, updated_at) VALUES (@order_id, 'name #3', 3, 30.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ORDER_ITEMS (order_id, name, quantity, price, created_at, updated_at) VaLUES (@order_id, 'name #4', 4, 40.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO ORDER_ITEMS (order_id, name, quantity, price, created_at, updated_at) VALUES (@order_id, 'name #5', 5, 50.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
