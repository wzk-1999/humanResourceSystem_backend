package com.blueOcean.humanResourceSystem.Utils;

import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasicLawSalaryImport {

    private static final Logger logger = LoggerFactory.getLogger(BasicLawSalaryImport.class);

    public static List<BasicLawSalary> readFromFile(String filePath) {
        List<BasicLawSalary> salaryList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0; // Initialize line counter
            // Skip the header line
            br.readLine();
            lineNumber++;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Split by tab
                String[] values = line.split("\t");
                try {
                    if (values.length == 7) {

                        // Encrypt the numeric properties
                        String encryptedBaseSalary = EncryptionUtils.encrypt(Double.parseDouble(values[3]));
                        String encryptedPerformanceBonus = EncryptionUtils.encrypt(Double.parseDouble(values[4]));
                        String encryptedApprenticeBonus = EncryptionUtils.encrypt(Double.parseDouble(values[5]));
                        String encryptedEvergreenBonus = EncryptionUtils.encrypt(Double.parseDouble(values[6]));

                        BasicLawSalary salary = new BasicLawSalary(
                                values[0],                          // TB_Staff_id
                                values[1],                          // Year
                                values[2],                          // Month
                                encryptedBaseSalary,      // Base_salary
                                encryptedPerformanceBonus,      // Performance_bonus
                                encryptedApprenticeBonus,      // Apprentice_bonus
                                encryptedEvergreenBonus       // Evergreen_bonus
                        );
                        salaryList.add(salary);
                    }else {
                        logger.warn("column not equals to 7, format error");
                    }
                } catch (Exception e) {
                    logger.warn("invalid file format at line {}",lineNumber);
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: {}", filePath, e);
        }
        return salaryList;
    }

}
