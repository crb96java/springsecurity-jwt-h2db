package com.springsecurity.jwt.controller;

import com.springsecurity.jwt.entity.AuthRequest;
import com.springsecurity.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/")
    public String welcome(){
        return "welcome...........";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception{

        try{
            //Below code to validate the token with username and password.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

        }catch (Exception ex){
            throw new Exception("Invalid username and password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}

