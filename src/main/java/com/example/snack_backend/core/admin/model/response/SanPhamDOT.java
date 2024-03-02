package com.example.snack_backend.core.admin.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDOT {

    private List<AdminImageResponse> img;
    private List<AdminSanPhamChiTiet2Response> sanPhamChiTiet;
    private  Integer id;
    private String ten;
    private String moTa;
    private String ma;
    private Integer trangThai;
    private String ngayTao;
    private Integer soLuongTon;
    private String quaiDeo;
    private String demLot;
    private String vatLieu;
    private String loai;
    private String thuongHieu;
    private String anh;
    private String ngaySua;


}
