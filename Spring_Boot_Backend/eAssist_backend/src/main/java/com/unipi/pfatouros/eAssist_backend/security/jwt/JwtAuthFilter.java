package com.unipi.pfatouros.eAssist_backend.security.jwt;

import com.unipi.pfatouros.eAssist_backend.security.auth.AppUserService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AppUserService appUserService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Get header
            String headerAuth = request.getHeader("Authorization");

            // Get token
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                // Remove "Bearer "
                String token = headerAuth.substring(7);

                // Check if token is valid
                if (jwtUtility.validateToken(token)) {

                    // Extract username from token
                    String username = jwtUtility.getUsernameFromToken(token);

                    // Create UserDetails object
                    UserDetails userDetails = appUserService.loadUserByUsername(username);

                    // Create UsernamePasswordAuthenticationToken object
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set user authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // Pass on the request and response to the next entity in the chain
        filterChain.doFilter(request, response);
    }
}
