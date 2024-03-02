package com.example.snack_backend.core.admin.model.response;

import org.springframework.beans.factory.annotation.Value;

public interface AdminImageResponse {
    @Value("#{target.anh}")
    String getAnh();

    @Value("#{target.id}")
    Integer getId();
}
