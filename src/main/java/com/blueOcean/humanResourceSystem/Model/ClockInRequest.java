package com.blueOcean.humanResourceSystem.Model;

import lombok.Data;

@Data
public class ClockInRequest {
    private String username;
    private String clockInTime;
    private Location location;
    private String locationName;
    private Byte isMorning;
    private String deviceInfo;
    private String photo; // Add this field to store the base64 image


    @Override
    public String toString() {
        return "ClockInRequest{" +
                "username='" + username + '\'' +
                ", clockInTime='" + clockInTime + '\'' +
                ", location=" + location +
                ", locationName='" + locationName + '\'' +
                ", isMorning=" + isMorning +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", photo='" + (photo != null ? "[image data]" : "null") + '\'' + // Mask photo data
                '}';
    }
}
