package com.expense.ExpenseTrackerApplication.JWT;

import com.expense.ExpenseTrackerApplication.Controller.AuthController;
import com.expense.ExpenseTrackerApplication.DTO.LoginRequest;
import com.expense.ExpenseTrackerApplication.DTO.RegisterRequest;
import com.expense.ExpenseTrackerApplication.Entity.Role;
import com.expense.ExpenseTrackerApplication.Entity.User;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public void register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        logger.info("Registration is done for user {}", request.getName());

    }

    public String login(LoginRequest request) {
        logger.info("Inside login");
        logger.info(" logging the user {}:", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        logger.info(" User Found in DB for {}:", request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.info("Password Miss Match");
            throw new BadCredentialsException("Invalid credentials");
        }
        logger.info("Password Missed for {}", request.getEmail());
        return jwtService.generateToken(user);
    }
}
