package com.example.snack_backend.core.admin.model.response;

import org.springframework.beans.factory.annotation.Value;

public interface AdminMauSacChiTietResponse {
    @Value("#{target.id}")
    Integer getId();

    @Value("#{target.ten}")
    String getTen();

    @Value("#{target.tenSize}")
    String getTenSize();

    @Value("#{target.soLuong}")
    String getSoLuong();

    @Value("#{target.anh}")
    String getAnh();
}
