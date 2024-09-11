package com.blueOcean.humanResourceSystem.Service;

import com.blueOcean.humanResourceSystem.Model.ClockInRecord;
import com.blueOcean.humanResourceSystem.Model.ClockInRequest;
import com.blueOcean.humanResourceSystem.Repository.ClockInRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ClockInConsumer {

    @Autowired
    private ClockInRecordRepository clockInRecordRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClockInConsumer.class);

    // Flag to control sync task start
    private final AtomicBoolean canProcessClockIn = new AtomicBoolean(false);

    @RabbitListener(queues = "clockInQueue")
    public void consumeClockInRecord(List<Map<String, ClockInRequest>> clockInDataList) {

        if (canProcessClockIn.get()) {
            List<ClockInRecord> records = new ArrayList<>();

            for (Map<String, ClockInRequest> clockInData : clockInDataList) {
                clockInData.forEach((key, value) -> {
                    ClockInRecord record = convertHashToClockInRecord(key, value);
                    records.add(record);
                });
            }

            // Batch insert into PostgreSQL
            clockInRecordRepository.saveAll(records);
            logger.info("Successfully saved {} records to the database.", records.size());
        }
    }

    @RabbitListener(queues = "deletionCompleteQueue")
    public void handleDeletionComplete(String message) {
        // Assuming the message indicates that the deletion task is complete
        logger.info("Received message from deletionCompleteQueue: {}", message);

        if ("Deletion complete".equals(message)) {
            // Set the flag to allow processing of clock-in records
            canProcessClockIn.set(true);
        } else {
            logger.warn("Unexpected message on deletionCompleteQueue: {}", message);
        }
    }

    private ClockInRecord convertHashToClockInRecord(String key, ClockInRequest value) {
        ClockInRecord record = new ClockInRecord();

        // Parse the key to extract username, clockInDate, and isMorning
        // Example key format: "username:MM/dd/yyyy:isMorning"
        String[] keyParts = key.split(":");
        if (keyParts.length == 3) {
            String username = keyParts[0];
            String dateStr = keyParts[1]; // Expected format: MM/dd/yyyy
            Boolean isMorning = "1".equals(keyParts[2]); // '1' means true (morning), '0' means false

            record.setUsername(username);
            // Parse dateStr into Date format
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                record.setClockInDate(java.sql.Date.valueOf(localDate)); // Convert LocalDate to Date
            } catch (Exception e) {
                logger.error("Error parsing clockInDate from key: {}", key, e);
            }

            // Set isMorning
            record.setIsMornig(isMorning);
        }

        // Set other fields from ClockInRequest value
        try {
            // Extract clockInTime
            String clockInTimeStr = value.getClockInTime();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy, HH:mm:ss");
            LocalDateTime clockInTime = LocalDateTime.parse(clockInTimeStr, dateTimeFormatter);
            record.setClockInTime(clockInTime);

            // Set location details
            if (value.getLocation() != null) {
                double latitude = value.getLocation().getLatitude();
                double longitude = value.getLocation().getLongitude();
                record.setLatitude(String.valueOf(latitude)); // Convert double to String
                record.setLongitude(String.valueOf(longitude)); // Convert double to String
            }

            // Set locationName and deviceInfo
            record.setLocationName(value.getLocationName());
            record.setDeviceInfo(value.getDeviceInfo());

        } catch (Exception e) {
            logger.error("Error converting ClockInRequest to ClockInRecord for key: {}", key, e);
        }

        return record;
    }
}
