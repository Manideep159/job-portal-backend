package com.example.Status.of.application.security;

import com.example.Status.of.application.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;


    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("RECEIVED TOKEN: " + token);


            try {
                String mobile = jwtUtil.extractUsername(token);

                if (mobile != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

                    if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                        System.out.println("TOKEN RECEIVED: " + token);

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid or expired token. Please login again.\"}");
                System.out.println("JWT ERROR: " + e.getMessage());
            }
        }

//        String header = request.getHeader("Authorization");
//
//        try {
//            if (header != null && header.startsWith("Bearer ")) {
//                String token = header.substring(7);
//
//                String username = jwtUtil.extractUsername(token);
//
//                if (username != null) {
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                    if (jwtUtil.validateToken(token, userDetails)) {
//                        UsernamePasswordAuthenticationToken authToken =
//                                new UsernamePasswordAuthenticationToken(
//                                        userDetails,
//                                        null,
//                                        userDetails.getAuthorities()
//                                );
//                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                        System.out.println("AUTH HEADER: " + request.getHeader("Authorization"));
//                        System.out.println("TOKEN EXTRACTED: " + token);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("JWT ERROR: " + e.getMessage()); // ✅ don't break request
//        }

        filterChain.doFilter(request, response);
    }
}
