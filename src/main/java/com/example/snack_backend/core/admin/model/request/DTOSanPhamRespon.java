package com.example.snack_backend.core.admin.model.request;

import com.example.demo.core.Admin.model.BaseDTO.BaseDTOSanPham;
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
