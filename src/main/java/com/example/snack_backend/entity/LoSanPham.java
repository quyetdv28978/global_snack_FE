package com.example.snack_backend.entity;

import com.example.snack_backend.entity.baseentity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@ToString
@Builder
public class LoSanPham extends BaseEntity {
    private String maLo;
    private String tenLo;
    private LocalDateTime ngayNhap;
    private int soLuongTon;
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private Double giaBan;
    private Double giaNhap;
    private BigDecimal tongTien;
    private int trangThai;

    @ManyToOne
    @JoinColumn(name = "id_ncc")
    private NhaCungCap nhaCungCap;
}
