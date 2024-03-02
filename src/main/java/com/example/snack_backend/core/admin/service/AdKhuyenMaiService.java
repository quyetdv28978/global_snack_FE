package com.example.snack_backend.core.admin.service;

import com.example.snack_backend.core.admin.model.request.AdminKhuyenMaiRequest;
import com.example.snack_backend.core.admin.model.response.AdminKhuyenMaiResponse;
import com.example.snack_backend.entity.ChiTietSanPham;
import com.example.snack_backend.entity.KhuyenMai;

import java.util.HashMap;
import java.util.List;

public interface AdKhuyenMaiService {

    List<AdminKhuyenMaiResponse> getAllKhuyenMai();

    HashMap<String, Object> add(AdminKhuyenMaiRequest khuyenMaiRequest);

    HashMap<String,Object> update(AdminKhuyenMaiRequest khuyenMaiRequest, Integer id);

    HashMap<String,Object> delete(AdminKhuyenMaiRequest khuyenMaiRequest, Integer id);

    KhuyenMai getKhuyenMaiById(Integer id);

    List<ChiTietSanPham> getAllSPCTByKhuyenMai();

    HashMap<String, Object>  updateProductDetail(Integer productId, Integer idkm);

}
