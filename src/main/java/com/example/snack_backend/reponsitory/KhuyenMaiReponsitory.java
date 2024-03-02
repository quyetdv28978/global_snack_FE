package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiReponsitory extends JpaRepository<KhuyenMai,Integer> {
}
