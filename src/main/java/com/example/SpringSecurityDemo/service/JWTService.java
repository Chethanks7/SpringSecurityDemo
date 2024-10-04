package com.example.SpringSecurityDemo.service;

import org.springframework.stereotype.Service;

@Service
public class JWTService {


    public String getToken() {
        return "this is your token";
    }
}
