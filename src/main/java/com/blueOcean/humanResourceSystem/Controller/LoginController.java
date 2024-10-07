package com.blueOcean.humanResourceSystem.Controller;

import com.blueOcean.humanResourceSystem.Annotation.LogMethod;
import com.blueOcean.humanResourceSystem.Model.UserCredentials;
import com.blueOcean.humanResourceSystem.Service.RoleService;
import com.blueOcean.humanResourceSystem.Service.UserService;
import com.blueOcean.humanResourceSystem.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/BO-hr-system")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping("/home")
    public String handleWelcome(){
        return "home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome(){
        return "home_admin";
    }

    @GetMapping("/user/home")
    public String handleUserHome(){
        return "home_User";
    }

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil

    @Autowired
    private RoleService roleService;

    @LogMethod("登录")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCredentials credentials) throws Exception {
        if (userService.login(credentials.getUsername(), credentials.getPassword())) {

            String[] role=roleService.getRoleByUsername(credentials.getUsername());
            String jwtToken = jwtUtil.generateToken(credentials.getUsername(),role);
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }
}
