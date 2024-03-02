package com.example.snack_backend.core.admin.model.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoSanPhamDTO {
    private String maLo;
    private String tenLo;
    private LocalDate ngayHetHan;
    private LocalDate ngaySanXuat;
    private LocalDateTime ngayNhap;
    private Integer soLuongTon;
    private Double giaNhap;
    private String tenSanPham;
    private Double tongTien;
    private int trangThai;
}
