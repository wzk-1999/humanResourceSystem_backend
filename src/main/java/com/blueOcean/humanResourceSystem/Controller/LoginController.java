package com.blueOcean.humanResourceSystem.Controller;

import com.blueOcean.humanResourceSystem.Model.userCredentials;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody userCredentials credentials) {
        if (userService.login(credentials.getUsername(), credentials.getPassword())) {
            String jwtToken = jwtUtil.generateToken(credentials.getUsername());
            return ResponseEntity.ok(jwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }
}
