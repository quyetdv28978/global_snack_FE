package com.example.snack_backend.core.admin.service.impl;


import com.example.snack_backend.core.admin.model.request.AdminDanhMucRequest;
import com.example.snack_backend.core.admin.repository.AdDanhMucReponsitory;
import com.example.snack_backend.core.admin.service.AdDanhMucService;
import com.example.snack_backend.entity.DanhMuc;
import com.example.snack_backend.util.DataUltil;
import com.example.snack_backend.util.DatetimeUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class DanhMucServiceImpl implements AdDanhMucService {

    @Autowired
    private AdDanhMucReponsitory danhMucReponsitory;


    @Override
    public Page<DanhMuc> getAll(Integer page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 5, sort);
        return danhMucReponsitory.findAll(pageable);
    }

    @Override
    public List<DanhMuc> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return danhMucReponsitory.findAll(sort);
    }

    @Override
    public List<DanhMuc> getAllByTrangThai(Integer trangThai) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return danhMucReponsitory.findAllByTrangThai(trangThai,sort);
    }

    @Override
    public DanhMuc getById(Integer id) {
        Optional<DanhMuc> optional = this.danhMucReponsitory.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public Page<DanhMuc> search(String keyword, Integer page) {
        if (keyword == null) {
            return this.getAll(page);
        } else {
            Pageable pageable = PageRequest.of(page, 5);
            return danhMucReponsitory.search("%" + keyword + "%", pageable);
        }
    }

    @Override
    public HashMap<String, Object> add(AdminDanhMucRequest dto) {
        DanhMuc danhMuc = dto.dtoToEntity(new DanhMuc());
        danhMuc.setTrangThai(1);
        try {
            DanhMuc danhMucs = danhMucReponsitory.save(danhMuc);
            danhMucs.setMa("TH" + danhMucs.getId());
            return DataUltil.setData("success", danhMucReponsitory.save(danhMucs));
        } catch (Exception e) {
            return DataUltil.setData("error", "error");
        }
    }

    @Override
    public HashMap<String, Object> update(AdminDanhMucRequest dto, Integer id) {
        Optional<DanhMuc> optional = danhMucReponsitory.findById(id);
        if (optional.isPresent()) {
            DanhMuc danhMuc = optional.get();
            danhMuc.setTen(dto.getTen());
            danhMuc.setNgaySua(DatetimeUtil.getCurrentDate());
            try {
                return DataUltil.setData("success", danhMucReponsitory.save(danhMuc));
            } catch (Exception e) {
                return DataUltil.setData("error", "error");
            }
        } else {
            return DataUltil.setData("error", "không tìm thấy thương hiệu sản phẩm để sửa");
        }
    }

    @Override
    public HashMap<String, Object> delete(Integer id) {
        Optional<DanhMuc> optional = danhMucReponsitory.findById(id);
        if (optional.isPresent()) {
            DanhMuc danhMuc = optional.get();
            danhMuc.setTrangThai(0);
            danhMuc.setNgaySua(DatetimeUtil.getCurrentDate());
            try {
                danhMucReponsitory.save(danhMuc);
                return DataUltil.setData("success", "xóa thành công");
            } catch (Exception e) {
                return DataUltil.setData("error", "error");
            }
        } else {
            return DataUltil.setData("error", "không tìm thấy thương hiệu sản phẩm để xóa");
        }
    }

    @Override
    public void saveExcel(MultipartFile file) {
        if (this.isValidExcelFile(file)) {
            try {
                List<DanhMuc> danhMucs = this.getCustomersDataFromExcel(file.getInputStream());
                List<DanhMuc> savedDanhMucs = this.danhMucReponsitory.saveAll(danhMucs);

                for (int i = 0; i < danhMucs.size(); i++) {
                    DanhMuc hieu = danhMucs.get(i);
                    hieu.setMa("TH" + savedDanhMucs.get(i).getId());
                    hieu.setNgayTao(DatetimeUtil.getCurrentDate());
                }

                this.danhMucReponsitory.saveAll(danhMucs);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

    public static boolean isValidExcelFile(MultipartFile file) {

        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public List<DanhMuc> getCustomersDataFromExcel(InputStream inputStream) {
        List<DanhMuc> danhMucList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(1);

            if (workbook != null) {
//                System.out.println("Workbook co ton tai");
                if (sheet != null) {
//                    System.out.println("sheet ton tai");
                    int rowIndex = 0;
                    for (Row row : sheet) {
                        if (rowIndex == 0) {
                            rowIndex++;
                            continue;
                        }
                        Iterator<Cell> cellIterator = row.iterator();
                        int cellIndex = 0;
                        DanhMuc danhMuc = new DanhMuc();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch (cellIndex) {
                                case 0 -> danhMuc.setTen(cell.getStringCellValue());
                                case 1 -> danhMuc.setTrangThai((int) cell.getNumericCellValue());
                                default -> {
                                }
                            }
                            cellIndex++;
                        }
                        danhMucList.add(danhMuc);
                    }
                } else {
                    System.out.println("sheet ko ton tai.");
                }
            } else {
                System.out.println("Workbook is null. Không thể đọc dữ liệu từ Excel.");
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
        return danhMucList;
    }
}
