package com.blueOcean.humanResourceSystem.Schedule;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class ClockInCleanupTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ClockInCleanupTask.class);


    @Scheduled(cron = "0 28 1 * * ?")  // Every day at 11:50 PM
    public void ClockInCleanup() {

                // Perform deletion logic
                // ...
                performDeletion();
                // Publish message after completion
                rabbitTemplate.convertAndSend("deletionCompleteQueue", "Deletion complete");
        }

    @Transactional
    public void performDeletion() {
        // Define the date threshold for records older than one month
        LocalDate thresholdDate = LocalDate.now().minusMonths(1);
        java.sql.Timestamp thresholdSqlDate = java.sql.Timestamp.valueOf(thresholdDate.atStartOfDay());

        try {
            // Use JdbcTemplate for direct SQL execution
            String sql = "DELETE FROM clock_in_record WHERE clock_in_date < ?";
            logger.info("Executing SQL: {}", sql);

            jdbcTemplate.update(sql, thresholdSqlDate);

            logger.info("Old records deleted successfully.");
        } catch (Exception e) {
            logger.error("Error occurred while deleting old records: ", e);
        }
    }
}

