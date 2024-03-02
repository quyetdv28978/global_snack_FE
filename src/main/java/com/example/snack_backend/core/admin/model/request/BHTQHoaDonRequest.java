package com.example.snack_backend.core.admin.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class BHTQHoaDonRequest {
    Integer idHTGH;
    Integer idPTTT;
    String moTa;
    BigDecimal tienKhachDua;
    Integer idDiaChi;
    Integer tienShip;
}
