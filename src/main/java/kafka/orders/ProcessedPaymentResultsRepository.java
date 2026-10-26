package kafka.orders;

import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class ProcessedPaymentResultsRepository {
    private final JdbcTemplate jdbc;

    public ProcessedPaymentResultsRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    public boolean markProcessed(String paymentId){
        try{
            jdbc.update("INSERT INTO processed_payment_results(payment_result_id, processed_at) VALUES (?,?)",
                    paymentId, Timestamp.from(Instant.now()));
            return true;
        } catch (DuplicateKeyException e){return false;}

    }
}
