package com.example.SpringSecurityDemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.example.SpringSecurityDemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import com.example.SpringSecurityDemo.service.UserService;

import java.util.List;

@RestController
public class DemoController {

    private final UserService userService;

    @Autowired
    public DemoController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String demo(){
        return "Hello World";
    }

    @GetMapping("get-all-users")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PostMapping("create-user")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
