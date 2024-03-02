package com.example.snack_backend.core.admin.model.response;

import org.springframework.beans.factory.annotation.Value;

public interface AdminThongKeThangNamTruocResponse {
    @Value("#{target.thang}")
    String getThang();

    @Value("#{target.tongTien}")
    Integer getTongTien();

    @Value("#{target.nam}")
    Integer getNam();
}
