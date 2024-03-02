package com.example.snack_backend.core.admin.controller;

import com.example.snack_backend.core.admin.service.impl.sanpham.SerSanPham;
import com.example.snack_backend.dto.SearchDTO.SearchSanPhamDTO;
import com.example.snack_backend.dto.request.DTOSanPhamResquest;
import com.example.snack_backend.util.Const;
import com.example.snack_backend.util.ConstFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/san-pham")
public class SanPhamApi {
    @Autowired
    private SerSanPham serSanPham;

//    @Autowired
//    private AdExcelAddSanPhamService adExcelAddSanPhamService;
//
//    @Autowired
//    private AdUpdateSanPhamService adUpdateSanPhamService;
//
//    // getAll loai
////    @GetMapping()
////    public ResponseEntity<?> getAll() {
////        List<AdminSanPhamResponse> page = sanPhamService.getAll();
////        return ResponseEntity.ok(page);
////    }
//
//    @GetMapping("")
//    public ResponseEntity<?> tesst() {
//        List<SanPhamDOT> page = sanPhamService.getAlls();
//        return ResponseEntity.ok(page);
//    }
//
//
//    @GetMapping("/by-loai")
//    public ResponseEntity<?> getSPByLoai(@RequestParam("idloai") Integer idloai) {
//        List<AdminSanPhamResponse> page = sanPhamService.getSanPhamByIdLoai(idloai);
//        return ResponseEntity.ok(page);
//    }
//
//    @GetMapping("/by-thuong-hieu")
//    public ResponseEntity<?> getSPByThuongHieu(@RequestParam("idthuonghieu") Integer idthuonghieu) {
//        List<AdminSanPhamResponse> page = sanPhamService.getSanPhamByIdThuongHieu(idthuonghieu);
//        return ResponseEntity.ok(page);
//    }
//
//    // thêm bằng file excel
//    @PostMapping("/view-data")
//    public ResponseEntity<?> viewDataImportExcel(@RequestParam("file") MultipartFile file) throws IOException {
//        return ResponseEntity.ok(adExcelAddSanPhamService.previewDataImportExcel(file));
//    }
//
//    @GetMapping("/{idSP}")
//    public ResponseEntity<?> getbySanPhamCT(@PathVariable Integer idSP) {
//        List<AdminSanPhamChiTiet2Response> page = sanPhamService.findBySanPhamCT(idSP);
//        return ResponseEntity.ok(page);
//    }
//
//    @GetMapping("/{idSP}/images")
//    public ResponseEntity<?> getbyImage(@PathVariable Integer idSP) {
//        List<AdminImageResponse> page = sanPhamService.getProductImages(idSP);
//        return ResponseEntity.ok(page);
//    }
//
//    @GetMapping("/loc")
//    public ResponseEntity<?> loc(@RequestParam String comboBoxValue) {
//        List<SanPhamDOT> lisst = sanPhamService.loc(comboBoxValue);
//        return ResponseEntity.ok(lisst);
//    }
//
//    @PutMapping("/check")
//    public ResponseEntity<?> check(@RequestBody AdminSanPhamRepuest2 request) {
//        return ResponseEntity.ok(sanPhamService.findBySanPhamTen(request));
//    }
//
//    @PostMapping()
//    public ResponseEntity<?> add(@RequestBody AdminSanPhamRepuest2 request) throws URISyntaxException, StorageException, InvalidKeyException, IOException {
//        SanPhamDOT save = sanPhamService.save(request);
//        return ResponseEntity.ok(save);
//    }
//
//    @PutMapping("/delete/{id}")
//    public ResponseEntity<?> delete(@PathVariable Integer id) {
//        SanPhamDOT sp = sanPhamService.delete(id);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PutMapping("/khoi-phuc/{id}")
//    public ResponseEntity<?> khoiPhuc(@PathVariable Integer id) {
//        SanPhamDOT sp = sanPhamService.khoiPhuc(id);
//        return ResponseEntity.ok(sp);
//    }
//
//
//    @PutMapping("/update-san-pham/{id}")
//    public ResponseEntity<?> updateSanPham(@PathVariable Integer id, @RequestBody AdminSanPhamRequest sanPhamRequest) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
//        SanPhamDOT sp = adUpdateSanPhamService.updateSanPham(id, sanPhamRequest);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PutMapping("/update-san-pham-CT/{id}")
//    public ResponseEntity<?> updateSanPhamCT(@PathVariable Integer id, @RequestBody AdminSanPhamChiTietRequest sanPhamRequest) throws IOException, StorageException, InvalidKeyException, URISyntaxException, ExecutionException, InterruptedException {
//        AdminSanPhamChiTiet2Response sp = adUpdateSanPhamService.update(sanPhamRequest, id);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PostMapping("/add-spct")
//    public ResponseEntity<?> add(@RequestBody AdminSanPhamChiTietRequest request) throws URISyntaxException, StorageException, InvalidKeyException, IOException {
//        AdminSanPhamChiTiet2Response save = adUpdateSanPhamService.saveSanPhamChiTiet(request);
//        return ResponseEntity.ok(save);
//    }
//
//    @PutMapping("/delete-spct/{id}")
//    public ResponseEntity<?> deleteSpct(@PathVariable Integer id) {
//        AdminSanPhamChiTiet2Response sp = adUpdateSanPhamService.delete(id);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PutMapping("/khoi-phuc-spct/{id}")
//    public ResponseEntity<?> khoiPhucSPCT(@PathVariable Integer id) {
//        AdminSanPhamChiTiet2Response sp = adUpdateSanPhamService.khoiPhuc(id);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PostMapping("/add-img/{idSP}")
//    public ResponseEntity<?> addImg(@PathVariable Integer idSP, @RequestBody AdminAddImageRequest adminAddImageRequest) throws URISyntaxException, StorageException, InvalidKeyException, IOException {
//        AdminImageResponse save = adUpdateSanPhamService.saveImage(idSP, adminAddImageRequest);
//        return ResponseEntity.ok(save);
//    }
//
//    @DeleteMapping("/delete-img/{idSP}")
//    public ResponseEntity<?> deleteImg(@PathVariable Integer idSP) {
//        AdminImageResponse sp = adUpdateSanPhamService.deleteImg(idSP);
//        return ResponseEntity.ok(sp);
//    }
//
//    @PutMapping("/update-img/{id}")
//    public ResponseEntity<?> updateImg(@PathVariable Integer id, @RequestBody AdminAddImageRequest dto) throws IOException, StorageException, InvalidKeyException, URISyntaxException {
//        AdminImageResponse sp = adUpdateSanPhamService.updateImage(id, dto);
//        return ResponseEntity.ok(sp);
//    }

    @GetMapping("{sl}/{page}/{trangThai}")
    public ResponseEntity showAllSanPhams(@PathVariable Integer sl, @PathVariable Integer page, @PathVariable Integer trangThai) {
        return ResponseEntity.ok().body(serSanPham.getSanPhamBy(sl, page, trangThai));
    }

    @GetMapping("not-discount")
    public ResponseEntity showSanPhamsDiscount() {
        return ResponseEntity.ok().body(serSanPham.getSanPhamDiscount());
    }

    @PostMapping("add")
    public ResponseEntity themSanPham(@RequestPart DTOSanPhamResquest dtoSanPhamResquest, @RequestPart MultipartFile[] multipartFiles) {
        ConstFile.updateLoadFile(multipartFiles);
        dtoSanPhamResquest.setImages(Arrays.stream(multipartFiles).map(i -> i.getOriginalFilename()).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(serSanPham.themSanPham(dtoSanPhamResquest));
    }

    @PutMapping("/{idctsp}")
    public ResponseEntity suaSanPham(
            @PathVariable Long idctsp,
            @RequestPart DTOSanPhamResquest dtoSanPhamResquest,
            @RequestPart MultipartFile[] multipartFiles) {
        ConstFile.updateLoadFile(multipartFiles);
        dtoSanPhamResquest.setImages(Arrays.stream(multipartFiles).map(i -> i.getOriginalFilename()).collect(Collectors.toSet()));
        return ResponseEntity.ok().body(serSanPham.updateSanPham(idctsp, dtoSanPhamResquest));
    }

    @GetMapping("search")
    public ResponseEntity searchByIdOrName(@RequestBody SearchSanPhamDTO searchSanPhamDTO) {
        return ResponseEntity.ok().body(serSanPham.searchSanPhamBy(searchSanPhamDTO.getName(), searchSanPhamDTO.getIdsp()));
    }

    @DeleteMapping("/chi-tiet/{idctsp}/{trangThai}")
    public ResponseEntity xoaSanPhamChiTiet(@PathVariable Long idctsp, @PathVariable Integer trangThai) {
        serSanPham.deleteSanPham(idctsp, trangThai);
        return ResponseEntity.ok().body(Const.DELETE_SUSSCESS);
    }

    @DeleteMapping("/{idsp}/{trangThai}")
    public ResponseEntity xoaSanPham(@PathVariable Long idsp, @PathVariable Integer trangThai) {
        serSanPham.deleteSanPhamReal(idsp, trangThai);
        return ResponseEntity.ok().body(Const.DELETE_SUSSCESS);
    }
}
