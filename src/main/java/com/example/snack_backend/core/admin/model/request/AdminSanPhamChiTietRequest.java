package com.example.snack_backend.core.admin.model.request;

import com.example.demo.entity.*;
import com.example.demo.infrastructure.adapter.DtoToEntity;
import com.example.demo.util.DatetimeUtil;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminSanPhamChiTietRequest implements DtoToEntity<SanPhamChiTiet> {

    private Integer idSP;

    private String anh;

    private Integer soLuongTon;

    private Integer giaBan;

 //   private Integer giaNhap;

    private Integer trongLuong;

    private String idKhuyenMai;

    private Integer mauSac;

    private Integer size;

    private Integer trangThai;

    @Override
    public SanPhamChiTiet dtoToEntity(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTiet.setNgayTao(DatetimeUtil.getCurrentDate());
        sanPhamChiTiet.setSanPham(SanPham.builder().id(this.getIdSP()).build());
        sanPhamChiTiet.setTrongLuong(TrongLuong.builder().id(this.getTrongLuong()).build());
        sanPhamChiTiet.setSoLuongTon(Integer.valueOf(this.getSoLuongTon()));
        sanPhamChiTiet.setGiaBan(BigDecimal.valueOf(Long.valueOf(this.getGiaBan())));
        if (this.getIdKhuyenMai() != null && this.getIdKhuyenMai() != "") {
            sanPhamChiTiet.setKhuyenMai(KhuyenMai.builder().id(Integer.valueOf(this.getIdKhuyenMai())).build());
        } else {
            sanPhamChiTiet.setKhuyenMai(null);
        }
        return sanPhamChiTiet;
    }
}
