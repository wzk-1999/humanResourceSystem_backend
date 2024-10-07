package com.blueOcean.humanResourceSystem.Repository;

import com.blueOcean.humanResourceSystem.Model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<RolePermission,String> {
    @Transactional(readOnly = true)
    RolePermission getRolePermissionsByRole(String role);  // Alough here getRolePermissionsByRole Role is upper case
    // but jpa still think the property name is role, and property naming is case-sensitive
}
