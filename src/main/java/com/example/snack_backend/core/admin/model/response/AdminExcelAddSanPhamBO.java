package com.example.snack_backend.core.admin.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminExcelAddSanPhamBO {
    private Long total;
    private Long totalSuccess;
    private Long totalError;
    private List<AdminExcelAddSanPhamResponse> responseList;
}
