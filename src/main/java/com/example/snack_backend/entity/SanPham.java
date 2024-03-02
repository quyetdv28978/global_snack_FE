package com.example.snack_backend.entity;

import com.example.snack_backend.dto.request.DTOSanPhamResquest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "san_pham")
@ToString
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "ten")
    private String tenSanPham;

    @Column(name = "ngay_sua")
    private String ngaySua;

    @Column(name = "ngay_tao")
    private String ngayTao;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "mo_ta", length = 10000)
    private String moTa;

    @Column(name = "huong_vi")
    private String huongVi;

    @ManyToOne
    @JoinColumn(name = "id_thuong_hieu")
    private ThuongHieu thuongHieu;

//    @OneToMany(mappedBy = "sanPham")
//    private Set<DanhMucChiTiet> danhMucChiTiets;

    @ManyToOne
    @JoinColumn(name = "id_danh_muc")
    private DanhMuc danhMuc;

    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ChiTietSanPham> sanPhamChiTietList;

    @JsonIgnore
    @OneToMany(mappedBy = "sanPham", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Comment> commentList;

//    private String tenSanPham;
//    private Double giaBan;
//    private int soLuongTon;
//    private String moTa, huongVi;
//    private Set<String> images;
//    private int trangThai;
//    private String thuongHieu;
    public SanPham mapperSanPham(DTOSanPhamResquest resquest) {
        return SanPham.builder()
                .tenSanPham(resquest.getTenSanPham())
                .moTa(resquest.getMoTa())
                .huongVi(resquest.getHuongVi())
                .trangThai(resquest.getTrangThai())
                .build();
    }
}
