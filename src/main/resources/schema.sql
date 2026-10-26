CREATE TABLE IF NOT EXISTS ordersRepository(
    order_id VARCHAR(64) PRIMARY KEY,
    status VARCHAR(20) NOT NULL CHECK (status IS IN  ("PENDING", "CONFIRMED", "CANCELLED")),
    payment_status VARCHAR(20) NOT NULL CHECK (status IS IN  ("PENDING", "CONFIRMED", "CANCELLED")),
    stock_status VARCHAR(20) NOT NULL CHECK (status IS IN  ("PENDING", "CONFIRMED", "CANCELLED"))
);

CREATE TABLE IF NOT EXISTS processed_payment_results (
    payment_result_id VARCHAR(64) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);
