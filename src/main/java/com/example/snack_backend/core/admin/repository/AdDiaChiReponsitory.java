package com.example.snack_backend.core.admin.repository;

import com.example.snack_backend.entity.DiaChi;
import com.example.snack_backend.reponsitory.DiaChiReponsitory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdDiaChiReponsitory extends DiaChiReponsitory {

    @Query("select u from DiaChi u where u.user.id =:id")
    List<DiaChi> findDiaChiByIdUser(Integer id);
}
