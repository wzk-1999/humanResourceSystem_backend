package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "BO_Staffs_info")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String TB_Staff_id;
    private String name;
    private String Department_id;
    private String Superior_Staff_id;

    public Staff() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTB_Staff_id() {
        return TB_Staff_id;
    }

    public void setTB_Staff_id(String TB_Staff_id) {
        this.TB_Staff_id = TB_Staff_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment_id() {
        return Department_id;
    }

    public void setDepartment_id(String department_id) {
        Department_id = department_id;
    }

    public String getSuperior_Staff_id() {
        return Superior_Staff_id;
    }

    public void setSuperior_Staff_id(String superior_Staff_id) {
        Superior_Staff_id = superior_Staff_id;
    }

    public Staff(Integer id, String TB_Staff_id, String name, String department_id, String superior_Staff_id) {
        this.id = id;
        this.TB_Staff_id = TB_Staff_id;
        this.name = name;
        Department_id = department_id;
        Superior_Staff_id = superior_Staff_id;
    }
}
