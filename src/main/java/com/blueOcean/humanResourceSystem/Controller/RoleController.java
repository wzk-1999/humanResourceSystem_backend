package com.blueOcean.humanResourceSystem.Controller;

import com.blueOcean.humanResourceSystem.Annotation.LogMethod;
import com.blueOcean.humanResourceSystem.DTO.HeaderManuResponse;
import com.blueOcean.humanResourceSystem.Service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.blueOcean.humanResourceSystem.Utils.JwtUtil.getRolesFromToken;

// if I direct modified some data in database, bypass hibernate, then after the next select request by hibernate,
// it will change/overwrite other data in that table, I don't know why
// I guess hibernate think same role_permission_role must have same manus, so when he finds the new record,
// it will update the other records with same role_permission_role

// 已解决，因为我直接用图形化界面改数据时，生成的sql是UPDATE public.role_permission_manus
//SET manus='profile', description='header'
//WHERE role_permission_role='Staff'; 根本不是我们想要的sql,和hibernate无关

@RestController
@RequestMapping("/Role")
@CrossOrigin(origins = "http://localhost:5173")
public class RoleController {
@Autowired
    private RoleService roleService;
    // API endpoint to get manus based on roles extracted from JWT token
    @LogMethod
    @GetMapping("/headerManus")
    public ResponseEntity<HeaderManuResponse> getHeaderManuByRoles(HttpServletRequest request) {
        // Extract the JWT from the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build(); // Unauthorized if token is missing
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        Set<String> roles = getRolesFromToken(token);

        // Pass the roles to the service
//        Set<String> manus = roleService.getHeaderManuByRoles(roles.toArray(new String[0]));
//        new String[0] , generic type to hint the return type, and will auto resize based on the size of set
        Set<String> manus = roleService.getHeaderManuByRoles(roles);

        Set<String> dropdown = roleService.getDropdownManuByRoles(roles);
        HeaderManuResponse headerManuResponse=new HeaderManuResponse();
        headerManuResponse.setManus(manus);
        headerManuResponse.setDropdown(dropdown);

        return ResponseEntity.ok(headerManuResponse);  // Return the Set of Manus as a response
    }
}
