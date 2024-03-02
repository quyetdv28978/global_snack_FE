package com.example.snack_backend.core.khachhang.service;

import com.example.snack_backend.core.khachhang.model.request.RegisterPayload;
import com.example.snack_backend.entity.User;

public interface RegisterService {

    User registerUser(RegisterPayload payload);
}
