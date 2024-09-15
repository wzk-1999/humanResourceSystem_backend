package com.blueOcean.humanResourceSystem.Model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class ClockInRecordId implements Serializable {
    private String username;
    private Date clockInDate;
    private Boolean isMorning;

    // Default constructor
    public ClockInRecordId() {}

    // Constructor
    public ClockInRecordId(String username, Date clockInDate, Boolean isMorning) {
        this.username = username;
        this.clockInDate = clockInDate;
        this.isMorning = isMorning;
    }

    // Getters and Setters (optional, but useful)

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockInRecordId that = (ClockInRecordId) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(clockInDate, that.clockInDate) &&
                Objects.equals(isMorning, that.isMorning);
    }

    // keep consistency, when objects are equal, their hashcode must be equal, in case scenario using hash table
    @Override
    public int hashCode() {
        return Objects.hash(username, clockInDate, isMorning);
    }
}
