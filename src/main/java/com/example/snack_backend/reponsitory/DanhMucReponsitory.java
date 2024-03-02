package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.DanhMuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhMucReponsitory extends JpaRepository<DanhMuc, Integer> {

    List<DanhMuc> findAllByTrangThai(Integer trangThai, Sort sort);

}

