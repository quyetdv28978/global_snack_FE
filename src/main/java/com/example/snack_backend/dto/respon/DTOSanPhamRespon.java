package com.example.snack_backend.dto.respon;

import com.example.snack_backend.dto.basedto.BaseDTOSanPham;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DTOSanPhamRespon extends BaseDTOSanPham {
    private String _tenDanhMuc;
    private Set<String> _trongLuong;
    private String _tenThuongHieu;
}
