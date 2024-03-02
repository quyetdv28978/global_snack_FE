package com.example.snack_backend.util;


import com.example.snack_backend.dto.request.DTOSanPhamResquest;
import com.example.snack_backend.dto.respon.DTOSanPhamRespon;
import com.example.snack_backend.entity.DanhMuc;
import com.example.snack_backend.entity.SanPham;
import com.example.snack_backend.entity.ThuongHieu;

import java.util.stream.Collectors;

public class ConstSanPhamRespon {
    public static DTOSanPhamRespon responSanPham(DanhMuc danhMuc,
                                                 SanPham sanPham, ThuongHieu thuongHieu, DTOSanPhamResquest resquest) {
        DTOSanPhamRespon dtoSanPhamRespon = MapperUtils.dtoToEntity(resquest, DTOSanPhamRespon.class);

        dtoSanPhamRespon.setTenSanPham(sanPham.getTenSanPham());
        dtoSanPhamRespon.setTrangThai(sanPham.getTrangThai());
        dtoSanPhamRespon.setHuongVi(sanPham.getHuongVi());

        if (danhMuc != null)
        dtoSanPhamRespon.set_tenDanhMuc(danhMuc.getTen());
        dtoSanPhamRespon.set_trongLuong(sanPham.getSanPhamChiTietList().stream()
                .map(i -> i.getTrongLuong().getValue() + " " +i.getTrongLuong().getDonVi())
                .collect(Collectors.toSet()));
        dtoSanPhamRespon.set_tenThuongHieu(thuongHieu.getTen());
        return dtoSanPhamRespon;
    }
}
