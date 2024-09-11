package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_Credentials")
public class userCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String initial_password;
    private Boolean is_initial_password;
    private String tb_staff_id;
    private String role;

    public String getTb_staff_id() {
        return tb_staff_id;
    }

    public void setTb_staff_id(String tb_staff_id) {
        this.tb_staff_id = tb_staff_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitial_password() {
        return initial_password;
    }

    public void setInitial_password(String initial_password) {
        this.initial_password = initial_password;
    }

    public Boolean getIs_initial_password() {
        return is_initial_password;
    }

    public void setIs_initial_password(Boolean is_initial_password) {
        this.is_initial_password = is_initial_password;
    }

    public userCredentials(Integer id, String username, String password, String initial_password, Boolean is_initial_password, String tb_staff_id, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.initial_password = initial_password;
        this.is_initial_password = is_initial_password;
        this.tb_staff_id = tb_staff_id;
        this.role = role;
    }

    public userCredentials() {
    }
}
