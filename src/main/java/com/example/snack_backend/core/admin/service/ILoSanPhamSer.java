package com.example.snack_backend.core.admin.service;

import com.example.snack_backend.dto.request.LoSanPhamDTO;
import com.example.snack_backend.entity.ChiTietSanPham;
import com.example.snack_backend.entity.LoSanPham;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILoSanPhamSer {
    List<LoSanPhamDTO> loSanPhams(Long idsp, int trangThai);

    LoSanPham themLoSanPham(LoSanPhamDTO loSanPhamDTO, ChiTietSanPham ctSanPham);

}
