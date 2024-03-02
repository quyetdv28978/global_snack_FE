package com.example.snack_backend.core.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BHTQPhuongThucThanhToanResponse {
    private Integer id;
    private String ten;
    private String ma;
    private String ngaySua;
    private String ngayTao;
    private Integer trangThai;
}
