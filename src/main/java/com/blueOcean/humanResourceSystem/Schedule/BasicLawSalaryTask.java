package com.blueOcean.humanResourceSystem.Schedule;

import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import com.blueOcean.humanResourceSystem.Repository.BasicLawSalaryRepository;
import com.blueOcean.humanResourceSystem.Utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class BasicLawSalaryTask {
    private static final Logger logger = LoggerFactory.getLogger(BasicLawSalaryTask.class);

    @Autowired
    private BasicLawSalaryRepository repository;

    private static final int BATCH_SIZE = 1000; // Define a reasonable batch size

    // Scheduled to run every day at midnight (adjust as needed)
    @Scheduled(cron = "0 31 20 * * ?")
    @Transactional
    public void processAndSaveSalaries() {
        String filePath = "src/main/resources/static/Base_Salary_202303.txt"; // Adjust as needed

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            Stream<String> lines = br.lines().skip(1);

            List<BasicLawSalary> batch = new ArrayList<>();
            lines.forEach(line -> {
                try {
                    String[] values = line.split("\t");
                    if (values.length == 7) {
                        BasicLawSalary salary = new BasicLawSalary(
                                values[0],                          // TB_Staff_id
                                values[1],                          // Year
                                values[2],                          // Month
                                EncryptionUtils.encrypt(Double.parseDouble(values[3])),      // Base_salary
                                EncryptionUtils.encrypt(Double.parseDouble(values[4])),      // Performance_bonus
                                EncryptionUtils.encrypt(Double.parseDouble(values[5])),      // Apprentice_bonus
                                EncryptionUtils.encrypt(Double.parseDouble(values[6]))       // Evergreen_bonus
                        );
                        batch.add(salary);

                        // Save batch when it reaches BATCH_SIZE
                        if (batch.size() == BATCH_SIZE) {
                            repository.saveAll(batch);
                            logger.info("Successfully processed and saved a batch of {} salaries to the database.", batch.size());
                            batch.clear();
                        }
                    } else {
                        logger.warn("Invalid format at line: {}", line);
                    }
                } catch (Exception e) {
                    logger.error("Error processing line: {}", line, e);
                }
            });

            // Save any remaining records in the final batch
            if (!batch.isEmpty()) {
                repository.saveAll(batch);
                logger.info("Successfully processed and saved the final batch of {} salaries to the database.", batch.size());
            }

        } catch (IOException e) {
            logger.error("Error reading file: {}", filePath, e);
        }
    }
}
