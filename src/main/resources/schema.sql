CREATE TABLE IF NOT EXISTS ordersRepository(
    order_id VARCHAR(64) PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    stock_status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS processed_payment_results (
    payment_result_id VARCHAR(64) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);
