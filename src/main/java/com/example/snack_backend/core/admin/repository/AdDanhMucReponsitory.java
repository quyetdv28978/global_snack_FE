package com.example.snack_backend.core.admin.repository;

import com.example.snack_backend.entity.DanhMuc;
import com.example.snack_backend.reponsitory.DanhMucReponsitory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdDanhMucReponsitory extends DanhMucReponsitory {
    @Query("select  pot from  ThuongHieu  pot where pot.ten like :keyword or pot.ma like :keyword")
    Page<DanhMuc> search(String keyword, Pageable pageable);

    List<DanhMuc> findAllByTrangThai(Integer trangThai, Sort sort);

    DanhMuc findByTen(String ten);

}
