package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class ClockInRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Date clockInDate;
    private Boolean isMornig;

    private LocalDateTime clockInTime;
    private String latitude;
    private String longitude;
    private String locationName;
    private String deviceInfo;

}
