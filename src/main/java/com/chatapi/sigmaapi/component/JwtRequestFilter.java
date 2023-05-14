package com.chatapi.sigmaapi.component;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chatapi.sigmaapi.service.register.JwtService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private String infoUserDetailsTemp = "user";
    private String headerToken = "Bearer ";
    private String keyHeaderToken = "token";

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = request.getHeader(keyHeaderToken);
        String jwt = null;
        if (token != null && token.startsWith(headerToken)) {
            jwt = token.substring(7);
            try {
                if (jwtService.validateToken(jwt)) {
                    User userDetails = new User(infoUserDetailsTemp, infoUserDetailsTemp, new ArrayList<>());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            } catch (Exception e) {
                logger.info(e);
            }

        }
        filterChain.doFilter(request, response);
    }

}
