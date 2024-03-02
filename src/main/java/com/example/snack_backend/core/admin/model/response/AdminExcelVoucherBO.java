package com.example.snack_backend.core.admin.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AdminExcelVoucherBO {
    private Long total;
    private Long totalSuccess;
    private Long totalError;
    private List<AdminExcelVoucherResponse> responseList;
}
