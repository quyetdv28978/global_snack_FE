package com.example.snack_backend.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DTOSanPhamDIscount {
    private Long id;
    private Double giaBan;
    private String moTa, tenSanPham, tieuDe;
    private int soLuongTon;
}
