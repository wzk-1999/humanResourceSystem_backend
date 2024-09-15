package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BasicSalaryId.class)  // Specify the composite key class
public class BasicLawSalary {
    @Id
    private String TB_Staff_id;
    @Id
    private String Year;
    @Id
    private String Month;
    private String Base_salary;
    private String Performance_bonus;
    private String Apprentice_bonus;
    private String Evergreen_bonus;

    @Override
    public String toString() {
        return "BasicLawSalary{" +
                "TB_Staff_id='" + TB_Staff_id + '\'' +
                ", Year='" + Year + '\'' +
                ", Month='" + Month + '\'' +
                ", Base_salary=" + Base_salary +
                ", Performance_bonus=" + Performance_bonus +
                ", Apprentice_bonus=" + Apprentice_bonus +
                ", Evergreen_bonus=" + Evergreen_bonus +
                '}';
    }

    public BasicLawSalary(String year,String month){
        this.Year=year;
        this.Month=month;
    }
}
