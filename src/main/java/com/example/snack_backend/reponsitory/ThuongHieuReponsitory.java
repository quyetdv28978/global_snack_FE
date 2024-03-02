package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThuongHieuReponsitory extends JpaRepository<ThuongHieu, Long> {

    List<ThuongHieu> findAllByTrangThai(Integer trangThai, Sort sort);

}

