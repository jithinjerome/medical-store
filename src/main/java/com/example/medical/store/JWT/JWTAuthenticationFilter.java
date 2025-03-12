package com.example.medical.store.JWT;

import com.example.medical.store.User.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {


    private final CustomerUserDetailsService customerUserDetailsService;

    private final UserService userService;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(@Lazy UserService userService, JWTUtil jwtUtil, CustomerUserDetailsService customerUserDetailsService){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
                System.out.println("User Authorities: " + userDetails.getAuthorities());
                String role = jwtUtil.extractClaim(token, claims -> claims.get("role", String.class));
//                if (role.startsWith("ROLE_")) {
//                    role = role.substring(5); // Extract "MEDICALSTORE" from "ROLE_MEDICALSTORE"
//                }
       //         System.out.println("Extracted Role from JWT: " + role);

                if(jwtUtil.validateToken(token,username)){

                    var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,List.of(new SimpleGrantedAuthority(role)));
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println("Auth Token Granted Authorities: " + authToken.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        }
        chain.doFilter(request,response);
    }
}
