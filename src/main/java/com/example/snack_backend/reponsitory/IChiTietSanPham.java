package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.ChiTietSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChiTietSanPham extends JpaRepository<ChiTietSanPham, Long> {
}
