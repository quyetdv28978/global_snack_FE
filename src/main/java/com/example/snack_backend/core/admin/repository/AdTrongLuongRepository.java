package com.example.snack_backend.core.admin.repository;

import com.example.snack_backend.entity.TrongLuong;
import com.example.snack_backend.reponsitory.TrongLuongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdTrongLuongRepository extends TrongLuongRepository {
    @Query("select  pot from  TrongLuong  pot where pot.donVi like :keyword or pot.ma like :keyword")
    Page<TrongLuong> search(String keyword, Pageable pageable);
    List<TrongLuong> findAllByTrangThai(Integer trangThai, Sort sort);

//    @Query("select  pot from  TrongLuong  pot " +
//            "where pot.value like :keyword ")
//    TrongLuong findByTenTrongLuongExcel(Integer keyword);
}

