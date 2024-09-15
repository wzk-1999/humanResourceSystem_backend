package com.blueOcean.humanResourceSystem.DTO;

import com.blueOcean.humanResourceSystem.Model.BasicLawSalary;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SalaryResponse {
    private List<BasicLawSalary> records;
    private long totalRecords;

}
