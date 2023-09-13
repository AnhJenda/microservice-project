package com.example.userservice.controller;

import com.example.userservice.entities.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    @author: Dinh Quang Anh
    Date   : 9/6/2023
    Project: user-service
*/
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping(value = "/secure")
    public String getSecure() {
        return "Secure endpoint available";
    }
}
