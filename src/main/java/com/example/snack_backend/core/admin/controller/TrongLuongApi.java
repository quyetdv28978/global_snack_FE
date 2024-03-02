package com.example.snack_backend.core.admin.controller;


import com.example.snack_backend.core.admin.model.request.AdminTrongLuongRequest;
import com.example.snack_backend.entity.TrongLuong;
import com.example.snack_backend.core.admin.service.impl.TrongLuongServiceImpl;
import com.example.snack_backend.util.DataUltil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/trong-luong")
@CrossOrigin(origins = {"*"})
public class TrongLuongApi {
    @Autowired
    private TrongLuongServiceImpl trongLuongService;
//    @GetMapping()
//    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0", value = "pages") Integer pages) {
//        Page<Size> page = sizeService.getAll(pages);
//        HashMap<String, Object> map = DataUltil.setData("ok", page);
//        return ResponseEntity.ok(map);
//    }

    // hiển thị tất cả
    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<TrongLuong> page = trongLuongService.findAll();
        HashMap<String, Object> map = DataUltil.setData("ok", page);
        return ResponseEntity.ok(map);
    }

    // hiển thị theo trạng thái
    @GetMapping("/trang-thai")
    public ResponseEntity<?> getAllByTrangThai(@RequestParam("trangThai") Integer trangThai) {
        List<TrongLuong> page = trongLuongService.getAllByTrangThai(trangThai);
        HashMap<String, Object> map = DataUltil.setData("ok", page);
        return ResponseEntity.ok(map);
    }

    // check validate
    @PostMapping("/validation")
    public ResponseEntity<?> validation(@RequestBody @Valid AdminTrongLuongRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            HashMap<String, Object> map = DataUltil.setData("error", list);
            return ResponseEntity.ok(map);
        } else {
            HashMap<String, Object> map = DataUltil.setData("ok", "");
            return ResponseEntity.ok(map);
        }
    }

    // thêm
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody AdminTrongLuongRequest request) {
        HashMap<String, Object> map = trongLuongService.add(request);
        return ResponseEntity.ok(map);
    }

    // sửa
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody AdminTrongLuongRequest request) {
        HashMap<String, Object> map = trongLuongService.update(request, id);
        return ResponseEntity.ok(map);
    }

    // xóa (đổi trạng thái về 0)
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        HashMap<String, Object> map = trongLuongService.delete(id);
        return ResponseEntity.ok(map);
    }

    // thêm bằng file excel
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        this.trongLuongService.saveExcel(file);
        HashMap<String, Object> map = DataUltil.setData("success", " thêm size thành công");
        return ResponseEntity.ok(map);
    }

    //đã code
}

