package com.example.snack_backend.core.admin.model.request;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.User;
import com.example.demo.infrastructure.adapter.DtoToEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDiaChiRequest  implements DtoToEntity<DiaChi> {

    private Integer idUser;

    private Integer idTinhThanh;

    private String tinhThanh;

    private Integer idQuanHuyen;

    private String quanHuyen;

    private String idPhuongXa;

    private String phuongXa;

    private String diaChi;

    @Override
    public DiaChi dtoToEntity(DiaChi e) {
        e.setUser(User.builder().id(this.getIdUser()).build());
        e.setIdTinhThanh(this.getIdTinhThanh());
        e.setTenTinhThanh(this.getTinhThanh());
        e.setIdQuanHuyen(this.getIdQuanHuyen());
        e.setTenQuanHuyen(this.getQuanHuyen());
        e.setIdphuongXa(this.getIdPhuongXa());
        e.setTenphuongXa(this.getPhuongXa());
        e.setDiaChi(this.getDiaChi());
        return e;
    }
}
