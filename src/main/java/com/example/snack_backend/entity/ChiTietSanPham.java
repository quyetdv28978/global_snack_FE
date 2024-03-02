package com.example.snack_backend.entity;

import com.example.snack_backend.entity.baseentity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@ToString
public class ChiTietSanPham extends BaseEntity {

    private Integer soLuongTon;

    private Double giaBan;

    private Double giaNhap;

    private int trangThai;

//    @Column(name = "gia_sau_giam", precision = 20, scale = 0)
//    private BigDecimal giaSauGiam;

    @ManyToOne
    @JoinColumn(name = "id_san_pham")
    @ToString.Exclude
    @JsonIgnore
    private SanPham sanPham;

    @OneToOne
    @JoinColumn(name = "id_lo_san_pham")
    private LoSanPham loSanPham;

    @ManyToOne
    @JoinColumn(name = "id_trong_luong")
    private TrongLuong trongLuong;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_thong_bao")
    private ThongBao thongBao;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Image> images;
}
