package com.blueOcean.humanResourceSystem.Service;

import com.blueOcean.humanResourceSystem.DTO.SalaryResponse;
import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import com.blueOcean.humanResourceSystem.Repository.BasicLawSalaryRepository;
import com.blueOcean.humanResourceSystem.Utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasicLawSalaryService {

    @Autowired
    private BasicLawSalaryRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(BasicLawSalaryService.class);

    public Double decryptSalary(String year, String month, String username, String salaryType) {

        // Fetch the record based on year, month, and username
        BasicLawSalary basicLawSalary = repository.findByYearMonthAndUsername(year, month, username);

        if (basicLawSalary == null) {
            logger.warn("No record found for year: {}, month: {}, username: {}", year, month, username);
            return null;
        }

        // Determine which salary type to decrypt
        String encryptedValue;
        switch (salaryType.toLowerCase()) {
            case "base_salary":
                encryptedValue = basicLawSalary.getBase_salary().toString();
                break;
            case "evergreen_bonus":
                encryptedValue = basicLawSalary.getEvergreen_bonus().toString();
                break;
            case "apprentice_bonus":
                encryptedValue = basicLawSalary.getApprentice_bonus().toString();
                break;
            case "performance_bonus":
                encryptedValue = basicLawSalary.getPerformance_bonus().toString();
                break;
            default:
                logger.warn("Invalid salary type: {}", salaryType);
                throw new IllegalArgumentException("Invalid salary type: " + salaryType);
        }

        // Decrypt the salary
        try {
            return EncryptionUtils.decrypt(encryptedValue);
        } catch (Exception e) {
            logger.warn("Decryption failed for value: {}", encryptedValue, e);
            throw new RuntimeException("Decryption failed", e);
        }
    }

    /**
     * Fetch salary records for a user with pagination and return the total count.
     *
     * @param username the username of the employee
     * @param page the page number
     * @param pageSize the size of each page
     * @return a SalaryResponse containing the records and total count
     */
    public SalaryResponse findBasicLawSalaryByUsername(String username, int page, int pageSize) {
        // Create a Pageable object with page and pageSize
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        // Fetch records from the database with pagination
        List<BasicLawSalary> basicLawSalaryList = repository.findBasicLawSalaryByUsername(username, pageable);

        // Get the total record count
        long totalRecords = repository.countByTB_Staff_id(username);

        // Prepare the response
        return new SalaryResponse(basicLawSalaryList, totalRecords);
    }
}
