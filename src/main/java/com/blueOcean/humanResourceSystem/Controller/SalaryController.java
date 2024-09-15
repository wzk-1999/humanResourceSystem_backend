package com.blueOcean.humanResourceSystem.Controller;

import com.blueOcean.humanResourceSystem.Annotation.LogMethod;
import com.blueOcean.humanResourceSystem.DTO.SalaryList;
import com.blueOcean.humanResourceSystem.DTO.SalaryRequest;
import com.blueOcean.humanResourceSystem.Service.BasicLawSalaryService;
import com.blueOcean.humanResourceSystem.Service.UserService;
import com.blueOcean.humanResourceSystem.Utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/Salary")
@CrossOrigin(origins = "http://localhost:5173")
public class SalaryController {

    @Autowired
    private UserService userService; // Service for user password verification

    @Autowired
    private BasicLawSalaryService basicLawSalaryService;

    @Autowired
    private JwtUtil jwtUtil;

    @LogMethod("查看真实薪资")
    @PostMapping("/View")
    public ResponseEntity<?> viewSalary(@RequestBody SalaryRequest salaryRequest, @RequestHeader("Authorization") String token) throws Exception {


        // 1. Validate JWT token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
        }

        // 2. Get username from JWT
        String username = jwtUtil.getUsernameFromToken(token);

        // log to the log file
        log.info("user {} has view the salary, type of {}, {}, {}",username,salaryRequest.getSalaryType(),salaryRequest.getYear(),salaryRequest.getMonth());

        // 3. Verify password
        if (!userService.login(username, salaryRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

//        System.out.println(salaryRequest);

        // 4.decrypt salary details
        Double decryptedSalary = basicLawSalaryService.decryptSalary(salaryRequest.getYear(),salaryRequest.getMonth(),
                username,salaryRequest.getSalaryType());

        return ResponseEntity.ok(decryptedSalary);
    }

    @LogMethod("查看薪资列表")
    @PostMapping("/Show")
    public ResponseEntity<?> showSalary(@RequestBody SalaryList salaryList, @RequestHeader("Authorization") String token) throws Exception {
        // 1. Validate JWT token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
        }

        // 2. Get username from JWT
        String username = jwtUtil.getUsernameFromToken(token);

        return ResponseEntity.ok(basicLawSalaryService.findBasicLawSalaryByUsername(username,salaryList.getPage(),salaryList.getPageSize()));
    }
}
