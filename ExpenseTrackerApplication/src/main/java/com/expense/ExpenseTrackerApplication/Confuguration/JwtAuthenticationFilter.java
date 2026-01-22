package com.expense.ExpenseTrackerApplication.Confuguration;

import com.expense.ExpenseTrackerApplication.JWT.JwtService;
import com.expense.ExpenseTrackerApplication.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Inside doFilterInternal 1");
        final String authHeader = request.getHeader("Authorization");
        log.info("Auth Header {}:",authHeader);
        log.info("Inside doFilterInternal 2");
        final String jwt;
        final String userEmail;
        log.info("Inside doFilterInternal 3");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Inside doFilterInternal 4");
            log.info("authHeader == null");
            filterChain.doFilter(request, response);
            log.info("Inside doFilterInternal 5");
            return;
        }

        jwt = authHeader.substring(7);
        log.info("Inside doFilterInternal 6");
        log.info("jwt {}:::",jwt);
        userEmail = jwtService.extractUsername(jwt);
        log.info("Inside doFilterInternal 7");
        log.info("Email is {}:",userEmail);


        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Inside doFilterInternal 8");
            log.info("SecurityContextHolder.getContext().getAuthentication() {}:",
                    SecurityContextHolder.getContext().getAuthentication());

            UserDetails userDetails = userRepository.findByEmail(userEmail).orElse(null);
            log.info("Inside doFilterInternal 9");

            if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
                log.info("Inside doFilterInternal 10");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                log.info("Inside doFilterInternal 11");
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Inside doFilterInternal 12");
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Inside doFilterInternal 13");
            }
        }

        filterChain.doFilter(request, response);
        log.info("Inside doFilterInternal 14");
    }
}
