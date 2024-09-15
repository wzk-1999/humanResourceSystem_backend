package com.blueOcean.humanResourceSystem.Model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class BasicSalaryId implements Serializable {
    private String TB_Staff_id;
    private String Year;
    private String Month;

    // Default constructor
    public BasicSalaryId() {}

    // Constructor
    public BasicSalaryId(String tB_Staff_id, String year, String month) {
        this.TB_Staff_id = tB_Staff_id;
        this.Year = year;
        this.Month = month;
    }

    // Getters and Setters (optional, but useful)

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicSalaryId that = (BasicSalaryId) o;
        return Objects.equals(TB_Staff_id, that.TB_Staff_id) &&
                Objects.equals(Year, that.Year) &&
                Objects.equals(Month, that.Month);
    }

    // keep consistency, when objects are equal, their hashcode must be equal, in case scenario using hash table
    @Override
    public int hashCode() {
        return Objects.hash(TB_Staff_id, Year, Month);
    }
}
