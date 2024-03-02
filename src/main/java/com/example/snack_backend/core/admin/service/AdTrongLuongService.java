package com.example.snack_backend.core.admin.service;


import com.example.snack_backend.core.admin.model.request.AdminTrongLuongRequest;
import com.example.snack_backend.entity.TrongLuong;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface AdTrongLuongService {

    Page<TrongLuong> getAll(Integer page);

    List<TrongLuong> findAll();

    List<TrongLuong> getAllByTrangThai(Integer trangThai);

    TrongLuong getById(Integer id);

    Page<TrongLuong> search(String keyword, Integer page);

    HashMap<String, Object> add(AdminTrongLuongRequest TrongLuong);

    HashMap<String,Object> update(AdminTrongLuongRequest trongLuongRequest, Integer id);

    HashMap<String,Object> delete(Integer id);

    void saveExcel(MultipartFile file);
}


