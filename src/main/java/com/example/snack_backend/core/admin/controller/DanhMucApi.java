package com.example.snack_backend.core.admin.controller;



import com.example.snack_backend.core.admin.model.request.AdminDanhMucRequest;
import com.example.snack_backend.entity.DanhMuc;
import com.example.snack_backend.core.admin.service.impl.DanhMucServiceImpl;
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
@RequestMapping("/api/admin/danh-muc")
@CrossOrigin(origins = {"*"})
public class DanhMucApi {

    @Autowired
    private DanhMucServiceImpl danhMucService;

//    @GetMapping()
//    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0", value = "pages") Integer pages) {
//        Page<ThuongHieu> page = thuongHieuService.getAll(pages);
//        HashMap<String, Object> map = DataUltil.setData("ok", page);
//        return ResponseEntity.ok(map);
//    }

    // hiển thị tất cả
    @GetMapping()
    public ResponseEntity<?> getAll() {
        List<DanhMuc> page = danhMucService.findAll();
        HashMap<String, Object> map = DataUltil.setData("ok", page);
        return ResponseEntity.ok(map);
    }

    //hiển thị theo trạng thái
    @GetMapping("/trang-thai")
    public ResponseEntity<?> getAllByTrangThai(@RequestParam("trangThai") Integer trangThai) {
        List<DanhMuc> page = danhMucService.getAllByTrangThai(trangThai);
        HashMap<String, Object> map = DataUltil.setData("ok", page);
        return ResponseEntity.ok(map);
    }

    // tim kiếm theo ten hoặc mã
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "0", value = "pages") Integer pages, @RequestParam(required = false) String keyword) {
        Page<DanhMuc> page = danhMucService.search(keyword, pages);
        HashMap<String, Object> map = DataUltil.setData("ok", "page");
        return ResponseEntity.ok(map);
    }

    // check validate
    @PostMapping("/validation")
    public ResponseEntity<?> validation(@RequestBody @Valid AdminDanhMucRequest request, BindingResult result) {
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
    public ResponseEntity<?> add(@RequestBody AdminDanhMucRequest request) {
        HashMap<String, Object> map = danhMucService.add(request);
        return ResponseEntity.ok(map);
    }

    // sửa
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody AdminDanhMucRequest request) {
        HashMap<String, Object> map = danhMucService.update(request, id);
        return ResponseEntity.ok(map);
    }

    // xóa (đổi trạng thái về 0)
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        HashMap<String, Object> map = danhMucService.delete(id);
        return ResponseEntity.ok(map);
    }

    // thêm bằng file excel
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        this.danhMucService.saveExcel(file);
        HashMap<String, Object> map = DataUltil.setData("success", " thêm sản phẩm thành công");
        return ResponseEntity.ok(map);
    }
}
