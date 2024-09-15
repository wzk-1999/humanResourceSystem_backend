package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class UserProfile {
    @Id
    private String TB_Staff_id;
    private String Department_id;
    private String Phone;
    @Lob  //indicate that this field will store large objects (LOB).
    @Column(columnDefinition = "TEXT") // For large strings
    private String base64Image;
    private String Address;
    private String Experience;
    private int age;
    private String University;
}
