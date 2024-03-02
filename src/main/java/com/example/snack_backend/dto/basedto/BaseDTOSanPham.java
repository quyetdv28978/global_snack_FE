package com.example.snack_backend.dto.basedto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseDTOSanPham {
    private String tenSanPham;
    private Double giaBan;
    private int soLuongTon;
    private String moTa, huongVi;
    private Set<String> images;
    private int trangThai;

    private String maLo;
    private String tenLo;
    private LocalDate ngayHetHan;
    private LocalDate ngaySanXuat;
    private LocalDate ngayNhap;
    private Double giaNhap;
}
