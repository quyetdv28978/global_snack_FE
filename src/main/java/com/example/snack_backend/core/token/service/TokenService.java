package com.example.snack_backend.core.token.service;

public interface TokenService {
    String genToken(String username);
    String getUserNameByToken(String token);
}
