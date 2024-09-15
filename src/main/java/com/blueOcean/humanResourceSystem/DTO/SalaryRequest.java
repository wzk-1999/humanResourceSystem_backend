package com.blueOcean.humanResourceSystem.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SalaryRequest {
    private String password;
    @JsonProperty("Year")
    private String Year;
    @JsonProperty("Month")
    private String Month;
    private String salaryType;
}
