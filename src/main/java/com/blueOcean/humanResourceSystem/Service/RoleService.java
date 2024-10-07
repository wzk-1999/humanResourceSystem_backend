package com.blueOcean.humanResourceSystem.Service;

import com.blueOcean.humanResourceSystem.Model.ManusItem;
import com.blueOcean.humanResourceSystem.Model.RolePermission;
import com.blueOcean.humanResourceSystem.Model.UserCredentials;
import com.blueOcean.humanResourceSystem.Repository.CredentialsRepository;
import com.blueOcean.humanResourceSystem.Repository.RoleRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService {
    @Resource
    private CredentialsRepository credentialsRepository;

    @Resource
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public String[] getRoleByUsername(String username) throws Exception {

        Optional<UserCredentials> userByusername = credentialsRepository.findUserCredentialsByUsername(username);
        if (userByusername.isPresent()) {
            UserService userService = new UserService();
            return userService.splitRole(userByusername.get());
        } else {
            throw new Exception("User does not exists");
        }
    }

//    JPA works within a persistence context, typically tied to a transaction.
//    If you're calling getRolePermissionsByRole() within a method
//    that does not have a @Transactional(readOnly = true) annotation,
//    JPA will assume that the entity is being managed and could trigger updates due to dirty
//        checking, even if no changes are made.

    @Transactional(readOnly = true)     //This will prevent JPA from considering the entities "dirty" and issuing unnecessary UPDATE statements.
    public Set<String> getHeaderManuByRoles(Set<String> roles) {
        Set<String> headerManus = new HashSet<>();

        for (String role : roles) {
            // Fetch RolePermission entity by role
            RolePermission rolePermission = roleRepository.getRolePermissionsByRole(role);

            // Detach the entity to prevent updates to it
//            if (rolePermission != null) {
//                entityManager.detach(rolePermission);
//            }


            // If the role exists and has Manus, split and add them to the Set
            if (rolePermission != null && rolePermission.getManus() != null) {
//                // Refresh the entity to ensure it's synchronized with the database
//                entityManager.refresh(rolePermission);
                // Assuming manus is stored as a comma-separated String
                List<ManusItem> manusList = rolePermission.getManus();
                for (ManusItem manu : manusList) {
                    if ("header".equals(manu.getDescription())) {
                        // Split the manus by commas and add each one to the set
                        String[] manuArray = manu.getManus().split(",");
                        for (String m : manuArray) {
                            headerManus.add(m.trim());  // Trim to remove unnecessary spaces
                        }
                    }
                }
            }
        }
        return headerManus;
    }

    @Transactional(readOnly = true)
    public Set<String> getDropdownManuByRoles(Set<String> roles) {
        Set<String> dropdownManus = new HashSet<>();

        for (String role : roles) {
            // Fetch RolePermission entity by role
            RolePermission rolePermission = roleRepository.getRolePermissionsByRole(role);

            // Detach the entity to prevent updates to it
//            if (rolePermission != null) {
//                entityManager.detach(rolePermission);
//            }


            // If the role exists and has Manus, split and add them to the Set
            if (rolePermission != null && rolePermission.getManus() != null) {

//                // Refresh the entity to ensure it's synchronized with the database
//                entityManager.refresh(rolePermission);
                // Assuming manus is stored as a comma-separated String
                List<ManusItem> manusList = rolePermission.getManus();
                for (ManusItem manu : manusList) {
                    if ("dropdown".equals(manu.getDescription())) {
                        // Split the manus by commas and add each one to the set
                        String[] manuArray = manu.getManus().split(",");
                        for (String m : manuArray) {
                            dropdownManus.add(m.trim());  // Trim to remove unnecessary spaces
                        }
                    }
                }
            }
        }
        return dropdownManus;
    }
}
