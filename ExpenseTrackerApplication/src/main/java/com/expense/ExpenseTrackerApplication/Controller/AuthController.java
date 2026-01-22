package com.expense.ExpenseTrackerApplication.Controller;

import com.expense.ExpenseTrackerApplication.DTO.LoginRequest;
import com.expense.ExpenseTrackerApplication.DTO.RegisterRequest;
import com.expense.ExpenseTrackerApplication.JWT.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
//@TO-DO--> add Exception for JWT
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        logger.info("Start Register User {}", request.getName());
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        logger.info("Logging User {}:",request.getEmail());
        String token = authService.login(request);
        logger.info("Logging done {}:",request.getEmail());
        return ResponseEntity.ok(token);
    }
}
