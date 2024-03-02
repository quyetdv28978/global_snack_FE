package com.example.snack_backend.core.khachhang.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    private Integer id;

    private String noiDung;

    private Integer sanPham;

    private Integer user;

    private Integer idPhanHoi;
}
