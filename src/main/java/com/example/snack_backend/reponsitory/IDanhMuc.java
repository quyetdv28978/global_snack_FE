package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDanhMuc extends JpaRepository<DanhMuc, Long> {
}
