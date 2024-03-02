package com.example.snack_backend.entity;

import com.example.snack_backend.entity.baseentity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "image")
public class Image extends BaseEntity {
    private String image;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_sanpham_ct")
    @ToString.Exclude
    private ChiTietSanPham sanPham;

}
