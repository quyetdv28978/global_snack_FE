package com.example.snack_backend.core.admin.service.impl.sanpham;
import com.example.snack_backend.core.admin.service.ILoSanPhamSer;
import com.example.snack_backend.dto.request.LoSanPhamDTO;
import com.example.snack_backend.entity.ChiTietSanPham;
import com.example.snack_backend.entity.LoSanPham;
import com.example.snack_backend.reponsitory.ILoSanPhamRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoSanPhamSer implements ILoSanPhamSer {
    @Autowired
    private ILoSanPhamRes loSanPhamRes;
//    @Autowired
//    private AutoMail autoMail;

    @Override
    public List<LoSanPhamDTO> loSanPhams(Long idsp, int trangThai) {
//        List<LoSanPhamDTO> loSanPhamDTO = loSanPhamRes.showAllLoSanPham(idsp, trangThai).stream()
//                .map(i -> MapperUtils.entityToDTO(i, LoSanPhamDTO.class))
//                .collect(Collectors.toList());
//        return loSanPhamDTO;
        return null;
    }

    @Override
    @Transactional
    public LoSanPham themLoSanPham(LoSanPhamDTO loSanPhamDTO, ChiTietSanPham ctSanPham) {
        LoSanPham loSanPham = loSanPhamRes.save(LoSanPham.builder()
                .ngayNhap(LocalDateTime.now())
                .ngaySanXuat(loSanPhamDTO.getNgaySanXuat())
                .ngayHetHan(loSanPhamDTO.getNgayHetHan())
                .soLuongTon(loSanPhamDTO.getSoLuongTon())
                .maLo(loSanPhamDTO.getMaLo())
                .giaBan(ctSanPham.getGiaBan())
                .giaNhap(loSanPhamDTO.getGiaNhap())
                .tongTien(new BigDecimal(loSanPhamDTO.getSoLuongTon()))
                .tenLo(loSanPhamDTO.getTenLo())
                .build());

        Long time = loSanPhamDTO.getNgaySanXuat().until(loSanPhamDTO.getNgayHetHan(), ChronoUnit.DAYS);
//        autoMail.checkOutDate(ConstMail.time, time);
    return loSanPham;
    }
}
