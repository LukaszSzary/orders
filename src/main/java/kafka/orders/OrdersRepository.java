package kafka.orders;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrdersRepository {
    private final JdbcTemplate jdbc;
    public OrdersRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public void registerOrder(String orderId){
        jdbc.update("INSERT INTO orders(order_id, status, payment_status, stock_status) VALUES (?, 'PENDING','PENDING','PENDING')", orderId);
    }

    public boolean changePaymentStatus(String orderId, String paymentStatus){
        int rows = jdbc.update("UPDATE orders SET payment_status = ? WHERE order_id = ?", paymentStatus,orderId);
        checkAndChangeOrderStatus(orderId);
        return rows == 1;
    }

    public boolean changeStockStatus(String orderId, String stockStatus){
        int rows = jdbc.update("UPDATE orders SET stock_status = ? WHERE order_id = ?", stockStatus,orderId);
        checkAndChangeOrderStatus(orderId);
        return rows == 1;
    }

    public String getOrderStatus(String orderId){
        return jdbc.queryForObject("SELECT status FROM ordersRepository WHERE order_id = ?", String.class, orderId);
    }

    private void checkAndChangeOrderStatus(String orderId){
        jdbc.update("UPDATE orders SET status = 'CONFIRMED' " +
                        "WHERE order_id = ? " +
                        "AND payment_status = 'CONFIRMED' " +
                        "AND stock_status = 'CONFIRMED'",
                orderId);
        jdbc.update("UPDATE orders SET status = 'CANCELLED' " +
                        "WHERE order_id = ? " +
                        "AND (payment_status = 'CANCELLED' " +
                        "OR stock_status = 'CANCELLED')",
                orderId);
    }


}
