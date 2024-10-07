package com.blueOcean.humanResourceSystem.Model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class RolePermission {
    @Id
    private String role;
    @ElementCollection
    @CollectionTable(name = "role_permission_manus")
    private List<ManusItem> manus;
    @ElementCollection
    @CollectionTable(name = "role_permission_buttons")
    private List<ButtonsItem> Buttons;
    @ElementCollection
    @CollectionTable(name = "role_permission_routers")
    private List<RoutersItem> Routers;
}
