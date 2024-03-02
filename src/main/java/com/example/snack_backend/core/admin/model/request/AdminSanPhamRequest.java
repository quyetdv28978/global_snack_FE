package com.example.snack_backend.core.admin.model.request;

import com.example.demo.entity.SanPham;
import com.example.demo.infrastructure.adapter.DtoToEntity;
import com.example.demo.util.DatetimeUtil;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Setter
public class AdminSanPhamRequest implements DtoToEntity<SanPham> {

    private Integer id;

    private String ten;

    private String moTa;

    private Integer trangThai;

    private Integer loai;

    private String anh;

    @Override
    public SanPham dtoToEntity(SanPham sanPham) {
        sanPham.setTenSanPham(this.getTen());
        sanPham.setNgayTao(DatetimeUtil.getCurrentDate());
        sanPham.setTrangThai(3);
        sanPham.setMoTa(this.getMoTa());
        return sanPham;
    }
}
