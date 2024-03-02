package com.example.snack_backend.core.admin.service;


import com.example.snack_backend.core.admin.model.request.AdminThuongHieuRequest;
import com.example.snack_backend.entity.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface AdThuongHieuService {
    Page<ThuongHieu> getAll(Integer page);

    List<ThuongHieu> findAll();

    List<ThuongHieu> getAllByTrangThai(Integer trangThai);

    ThuongHieu getById(Integer id);

    Page<ThuongHieu> search(String keyword, Integer page);

    HashMap<String, Object> add(AdminThuongHieuRequest dto);

    HashMap<String, Object> update(AdminThuongHieuRequest dto, Integer id);

    HashMap<String, Object> delete(Integer id);

    void saveExcel(MultipartFile file);
}
