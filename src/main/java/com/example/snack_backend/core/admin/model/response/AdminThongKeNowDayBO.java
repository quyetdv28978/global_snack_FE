package com.example.snack_backend.core.admin.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminThongKeNowDayBO {

    private Integer doanhThu;
    private Integer doanhThuTaiQuay;
    private Integer doanhThuOnline;
    private Integer donMua;
    private Integer donTra;
    private Integer donHuy;


}
