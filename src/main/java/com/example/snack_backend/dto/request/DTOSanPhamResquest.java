package com.example.snack_backend.dto.request;

import com.example.snack_backend.dto.basedto.BaseDTOSanPham;
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
