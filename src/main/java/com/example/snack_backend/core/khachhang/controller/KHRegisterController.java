package com.example.snack_backend.core.khachhang.controller;

import com.example.snack_backend.core.khachhang.model.request.RegisterPayload;
import com.example.snack_backend.core.khachhang.service.RegisterService;
import com.example.snack_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/khach-hang")
public class KHRegisterController {

    @Autowired
    RegisterService registerService;


    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterPayload payload){

        return registerService.registerUser(payload);

    }
}