    package com.example.snack_backend.entity;

import com.example.snack_backend.entity.baseentity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "nha_cung_cap")
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@ToString
@Builder
public class NhaCungCap extends BaseEntity {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChiNhaCung;
    private String sdt;
    private String email;
}
