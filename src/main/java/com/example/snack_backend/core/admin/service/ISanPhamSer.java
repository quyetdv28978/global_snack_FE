package com.example.snack_backend.core.admin.service;

import com.example.snack_backend.dto.request.DTOSanPhamResquest;
import com.example.snack_backend.dto.respon.DTOSanPhamRespon;
import com.example.snack_backend.entity.SanPham;

import java.util.List;

public interface ISanPhamSer {
    List getSanPhamBy(int soLuong, int trang, int trangThai);
    List getSanPhamDiscount();

    DTOSanPhamRespon themSanPham(DTOSanPhamResquest sanPham);

    DTOSanPhamRespon updateSanPham(Long idsp, DTOSanPhamResquest sanpham);

    void deleteSanPham(Long idsp, int trangThai);

    SanPham searchSanPhamBy(String name, Long idsp);

}
