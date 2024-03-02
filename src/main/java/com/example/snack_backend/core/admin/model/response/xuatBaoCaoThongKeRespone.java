package com.example.snack_backend.core.admin.model.response;


import org.springframework.beans.factory.annotation.Value;

public interface xuatBaoCaoThongKeRespone {

    @Value("#{target.thang}")
    String getThang();

    @Value("#{target.tong_tien_thang}")
    String getTongTien();

}
