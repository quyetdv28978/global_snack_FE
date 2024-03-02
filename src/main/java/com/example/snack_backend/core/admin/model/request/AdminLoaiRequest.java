package com.example.snack_backend.core.admin.model.request;

import com.example.demo.entity.DanhMuc;
import com.example.demo.infrastructure.adapter.DtoToEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminLoaiRequest implements DtoToEntity<DanhMuc> {

    private Integer id;

    @NotBlank(message = "Không bỏ trống tên")
    private String ten;

    private String ma;

    private String ngaySua;

    private String ngayTao;

    private Integer trangThai;


    @Override
    public DanhMuc dtoToEntity(DanhMuc danhMuc) {
        danhMuc.setMa(this.ma);
        danhMuc.setNameDanhMuc(this.ten);
        return danhMuc;
    }
}
