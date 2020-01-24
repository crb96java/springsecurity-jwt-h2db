package com.springsecurity.jwt.filter;

import com.springsecurity.jwt.service.CustomUserDetailsService;
import com.springsecurity.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader =httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZhdGVjaGllIiwiZXhwIjoxNTc5ODY4NTc3LCJpYXQiOjE1Nzk4MzI1Nzd9.OlLAX_2huZVwPYdPEUeAlhIYg3FvoDirH2SnbGWbaJE

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }
        if(userName !=null && SecurityContextHolder.getContext().getAuthentication() == null){
             UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
            logger.info("User Details "+ userDetails);

            //we are validating the Json web token if token is valid than we need to check the user details
            //User details is valid than we are just setting it to Security Context
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        logger.info("HttpServletRequest "+ httpServletRequest);
        logger.info("HttpServletResponse "+ httpServletResponse);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
