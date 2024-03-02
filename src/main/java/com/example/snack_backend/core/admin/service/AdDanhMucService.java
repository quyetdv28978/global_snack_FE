package com.example.snack_backend.core.admin.service;


import com.example.snack_backend.core.admin.model.request.AdminDanhMucRequest;
import com.example.snack_backend.entity.DanhMuc;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface AdDanhMucService {
    Page<DanhMuc> getAll(Integer page);

    List<DanhMuc> findAll();

    List<DanhMuc> getAllByTrangThai(Integer trangThai);

    DanhMuc getById(Integer id);

    Page<DanhMuc> search(String keyword, Integer page);

    HashMap<String, Object> add(AdminDanhMucRequest dto);

    HashMap<String, Object> update(AdminDanhMucRequest dto, Integer id);

    HashMap<String, Object> delete(Integer id);

    void saveExcel(MultipartFile file);
}
