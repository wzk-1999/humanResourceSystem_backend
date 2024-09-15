package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@IdClass(ClockInRecordId.class)  // Specify the composite key class

public class ClockInRecord {
    @Id
    private String username;
    @Id
    private Date clockInDate;
    @Id
    private Boolean isMorning;

    private LocalDateTime clockInTime;
    private String latitude;
    private String longitude;
    private String locationName;
    private String deviceInfo;

}
