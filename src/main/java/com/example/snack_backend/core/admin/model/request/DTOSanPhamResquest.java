package com.example.snack_backend.core.admin.model.request;

import com.example.demo.core.Admin.model.BaseDTO.BaseDTOSanPham;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DTOSanPhamResquest extends BaseDTOSanPham {
    private Long _idDanhMuc;
    private Long _idTrongLuong;
    private Long _idThuongHieu;
    private Long _idSanPham;
}
