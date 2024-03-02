package com.example.snack_backend.core.admin.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminThongKeBO {

    private Integer tongDoanhThu;

    private Integer tongHoanTien;

    private Integer tongChietKhau;

    private Integer tongDonHoanThanh;

    private List<AdminThongKeLoaiResponse> lstAdminThongKeLoaiResponses;

    private List<AdminThongKeSanPhamCaoResponse> lstAdminThongKeSanPhamCaoResponses;

    private List<AdminThongKeSanPhamThapResponse> lstAdminThongKeSanPhamThapResponses;

    private List<AdminThongKeThuongHieuResponse> lstAdminThongKeThuongHieuResponses;

    private List<AdminThongKeThangResponse> lstAdminThongKeThangResponses;

    private List<AdminThongKeThangNamTruocResponse> lstAdminThongKeThangNamResponses;


}
