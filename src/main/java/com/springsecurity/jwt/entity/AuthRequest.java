package com.springsecurity.jwt.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String userName;
    private String password;
}
