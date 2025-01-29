package com.url.shortner.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtilsProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //Extract token from header
            String jwt = jwtUtilsProvider.getJwtFromHeader(request);
            // validate the token
            //if valid -> get user details
            // get username -> load user -> set the auth context
            if(jwt != null && jwtUtilsProvider.validateToken(jwt)){
                String username = jwtUtilsProvider.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(userDetails != null){
                    // is typically used for programmatically authenticating a user
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // setDetails - Adds additional details to the authentication token. These details are usually related to the
                    // web request such as the user's session ID, remote IP address, or other contextual information.
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // SecurityContextHolder- This is a central class in Spring Security used to store and retrieve the SecurityContext.
                    // The SecurityContext holds the current authentication information for the application.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }


        } catch (Exception ex){
            ex.printStackTrace();
        }

        filterChain.doFilter(request, response);

    }
}
