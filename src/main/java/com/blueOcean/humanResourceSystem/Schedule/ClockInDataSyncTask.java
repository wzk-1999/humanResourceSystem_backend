package com.blueOcean.humanResourceSystem.Schedule;

import com.blueOcean.humanResourceSystem.Model.ClockInRequest;
import com.blueOcean.humanResourceSystem.Model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClockInDataSyncTask {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(ClockInDataSyncTask.class);

    private static final int BATCH_SIZE = 100;  // Define a suitable batch size

    @Scheduled(cron = "0 29 1 * * ?")  // Every day at 2 AM
    public void syncClockInData() {
        logger.info("Scheduled task started at: {}", LocalDateTime.now());

        LocalDate yesterday = LocalDate.now().minusDays(1);
        String yesterdayStr = yesterday.format(DateTimeFormatter.ofPattern("M/d/yyyy"));

        String pattern = "TB_*:" + yesterdayStr + ":[01]";  // Matches keys like username:MM/dd/yyyy:isMorning
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build();

        List<Map<String, ClockInRequest>> batchMessages = new ArrayList<>();
        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            List<String> keysBatch = new ArrayList<>();
            while (cursor.hasNext()) {
                keysBatch.add(cursor.next());
                if (keysBatch.size() == BATCH_SIZE) {
                    processKeysBatch(keysBatch, batchMessages);
                    keysBatch.clear();
                }
            }
            // Process remaining keys if not a full batch
            if (!keysBatch.isEmpty()) {
                processKeysBatch(keysBatch, batchMessages);
            }
        } catch (Exception e) {
            logger.error("Error during Redis SCAN operation: ", e);
        }

        // Send remaining batched messages
        if (!batchMessages.isEmpty()) {
            sendBatchMessages(batchMessages);
        }

        logger.info("Scheduled task completed at: {}", LocalDateTime.now());
    }

    private void processKeysBatch(List<String> keysBatch, List<Map<String, ClockInRequest>> batchMessages) {
        for (String key : keysBatch) {
            // Fetch the entire hash for the given Redis key
            Map<Object, Object> redisHash = redisTemplate.opsForHash().entries(key);
            ClockInRequest clockInRequest = convertToClockInRequest(redisHash);

            // Maintain the original key format (username:date:isMorning)
            Map<String, ClockInRequest> message = new HashMap<>();
            message.put(key, clockInRequest);
            batchMessages.add(message);

            // Send messages in batches
            if (batchMessages.size() == BATCH_SIZE) {
                sendBatchMessages(batchMessages);
                batchMessages.clear();
            }
        }
    }

    private ClockInRequest convertToClockInRequest(Map<Object, Object> redisHash) {
        ClockInRequest clockInRequest = new ClockInRequest();
        clockInRequest.setClockInTime((String) redisHash.get("clockInTime"));
        clockInRequest.setDeviceInfo((String) redisHash.get("deviceInfo"));
        clockInRequest.setLocationName((String) redisHash.get("locationName"));

        // Handle Location
        Object locationObject = redisHash.get("location");
        Location location = locationObject instanceof Location ? (Location) locationObject : null;
        clockInRequest.setLocation(location);

        return clockInRequest;
    }

    private void sendBatchMessages(List<Map<String, ClockInRequest>> batchMessages) {
        try {
            rabbitTemplate.convertAndSend("clockInQueue", batchMessages);
            logger.info("Batch of {} messages sent to the queue.", batchMessages.size());
        } catch (Exception e) {
            logger.error("Failed to send batch messages to queue: ", e);
        }
    }
}
