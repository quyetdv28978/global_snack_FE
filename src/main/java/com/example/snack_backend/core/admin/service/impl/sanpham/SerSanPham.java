package com.example.snack_backend.core.admin.service.impl.sanpham;

import com.example.snack_backend.core.admin.repository.AdThuongHieuReponsitory;
import com.example.snack_backend.core.admin.repository.AdTrongLuongRepository;
import com.example.snack_backend.core.admin.service.ISanPhamSer;
import com.example.snack_backend.dto.request.DTOSanPhamDIscount;
import com.example.snack_backend.dto.request.DTOSanPhamResquest;
import com.example.snack_backend.dto.request.LoSanPhamDTO;
import com.example.snack_backend.dto.respon.DTOSanPhamRespon;
import com.example.snack_backend.entity.*;
import com.example.snack_backend.entity.Image;
import com.example.snack_backend.entity.baseentity.BaseEntity;
import com.example.snack_backend.reponsitory.*;
import com.example.snack_backend.util.ConstSanPhamRespon;
import com.example.snack_backend.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("sanphamser")
public class SerSanPham implements ISanPhamSer {
    @Autowired
    private ISanPham resSanPham;
    @Autowired
    private com.example.snack_backend.reponsitory.Image imageRes;
    @Autowired
    private IDanhMuc danhMucRes;
    @Autowired
    private LoSanPhamSer loSanPhamSer;
    @Autowired
    private IChiTietSanPham chiTietSanPhamRes;
    @Autowired
    private AdThuongHieuReponsitory thuongHieuRepository;
    @Autowired
    private AdTrongLuongRepository trongLuongRespo;

    /*
    Param: soluong -> số lượng phần tử của 1 trang
    trang -> số trang trong tổng số sản phầm

    return -> danh sách sản phẩm với số lượng = soluong
     */
    @Override
    public List getSanPhamBy(int soLuong, int trang, int trangThai) {
        return resSanPham.findAllByTrangThai(trangThai, PageRequest.of(trang, soLuong));
    }

    public List<DTOSanPhamDIscount> getSanPhamDiscount() {
        return resSanPham.showSanPhamNotDiscount().stream()
                .map(i -> DTOSanPhamDIscount.builder()
                        .id((Long) i[0])
                        .tenSanPham((String) i[4])
                        .tieuDe((String) i[5])
                        .moTa((String) i[2])
                        .soLuongTon((int) i[3])
                        .giaBan((double) i[1])
                        .build()).collect(Collectors.toList());
    }

    /*
    Thêm sản phẩm
    Param: sanPham -> thuộc tính:tenSanPham;
    giaBan, soLuongTon, mota, tieuDe, images, idDanhMuc;

    return  -> sanpham
     */
    @Override
    @Transactional
    public DTOSanPhamRespon themSanPham(DTOSanPhamResquest sanPham) {
        SanPham sanPham1 = null;
        ThuongHieu thuongHieu = thuongHieuRepository.findById(sanPham.get_idThuongHieu()).get();
        TrongLuong trongLuong = trongLuongRespo.findById(sanPham.get_idTrongLuong()).get();
        DanhMuc danhMuc = null;

        if (sanPham.get_idSanPham() == null) {
            sanPham1 = new SanPham().mapperSanPham(sanPham);
            sanPham1.setThuongHieu(thuongHieu);
            sanPham1.setTrangThai(0);
            resSanPham.save(sanPham1);
        } else
            sanPham1 = resSanPham.findById(sanPham.get_idSanPham()).get();
        //       Them danh muc
        if (sanPham.get_idDanhMuc() != null) {
            danhMuc = danhMucRes.findById(sanPham.get_idDanhMuc()).get();
            sanPham1.setDanhMuc(danhMuc);
        }
//        Thêm lô sản phẩm
        LoSanPhamDTO loSanPhamDTO = MapperUtils.dtoToEntity(sanPham, LoSanPhamDTO.class);
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRes.save(MapperUtils.dtoToEntity(sanPham, ChiTietSanPham.class));
        LoSanPham loSanPham = loSanPhamSer.themLoSanPham(loSanPhamDTO, chiTietSanPham);
        chiTietSanPham.setLoSanPham(loSanPham);

//        Thêm trọng lượng

        chiTietSanPham.setTrongLuong(trongLuong);

//        Them san pham
        chiTietSanPham.setSanPham(sanPham1);

//        Them chi tiet san pham
        chiTietSanPham.setTrangThai(0);
        sanPham1.setSanPhamChiTietList(List.of(chiTietSanPhamRes.save(chiTietSanPham)));

//        them hinh anh
        saveIamges(sanPham, sanPham1.getSanPhamChiTietList().get(0).getId());

//        trả về san pham respon
        return ConstSanPhamRespon.responSanPham(danhMuc, sanPham1, thuongHieu, sanPham);
    }

    /*
        Sửa sản phẩm
    Param: sanPham -> thuộc tính:tenSanPham;
    giaBan, soLuongTon, mota, tieuDe, images, idDanhMuc;

    return  -> sanpham
     */
    @Override
    @Transactional
    public DTOSanPhamRespon updateSanPham(Long idctsp, DTOSanPhamResquest sanPham) {
        TrongLuong trongLuong = trongLuongRespo.findById(sanPham.get_idTrongLuong()).get();
        ThuongHieu thuongHieu = thuongHieuRepository.findById(sanPham.get_idThuongHieu()).get();
        DanhMuc danhMuc = null;
        SanPham sanPhamReal = chiTietSanPhamRes.findById(idctsp).get().getSanPham();

        if (sanPham.get_idDanhMuc() != null) {
            danhMuc = danhMucRes.findById(sanPham.get_idDanhMuc()).get();
            sanPhamReal.setDanhMuc(danhMuc);
        }
//        update san pham

        sanPhamReal.setHuongVi(sanPham.getHuongVi());
        sanPhamReal.setTenSanPham(sanPham.getTenSanPham());
        sanPhamReal.setMoTa(sanPham.getMoTa());
        sanPhamReal.setThuongHieu(thuongHieu);
        resSanPham.save(sanPhamReal);

        Integer idsp;
        //        update chi tiet san pham
        ChiTietSanPham sanPhamChiTiet = chiTietSanPhamRes.findById(idctsp).get();
        LoSanPham loSanPham = sanPhamChiTiet.getLoSanPham();
        idsp = sanPhamChiTiet.getSanPham().getId();
        sanPhamChiTiet = MapperUtils.dtoToEntity(sanPham, ChiTietSanPham.class);
        sanPhamChiTiet.getSanPham().setId(idsp);
        sanPhamChiTiet.setTrongLuong(trongLuong);
        sanPhamChiTiet.setLoSanPham(loSanPham);
        sanPhamChiTiet.setId(idctsp);
        chiTietSanPhamRes.save(sanPhamChiTiet);

        sanPhamReal.setSanPhamChiTietList(List.of(sanPhamChiTiet));

//        update images
        imageRes.deleteAllById(() -> chiTietSanPhamRes.findById(idctsp).get()
                .getImages().stream().map(BaseEntity::getId)
                .collect(Collectors.toSet()).iterator());
//        danhMucChiTietRes.deleteAllById(() -> sanPhamReal.getDanhMucChiTiets().stream().map(BaseEntity::getId).iterator());

        Set<Image> iamges = saveIamges(sanPham, idctsp);

        return ConstSanPhamRespon.responSanPham(danhMuc, sanPhamReal, thuongHieu, sanPham);
    }

    private Set<Image> saveIamges(DTOSanPhamResquest sanPham, Long idctsp) {
        return sanPham.getImages().stream().map(i ->
                imageRes.save(Image.builder()
                        .sanPham(chiTietSanPhamRes.findById(idctsp).get())
                        .image(i)
                        .build())
        ).collect(Collectors.toSet());
    }

    @Override
    public void deleteSanPham(Long idctsp, int trangThai) {
        ChiTietSanPham ctsSnPham = chiTietSanPhamRes.findById(idctsp).get();
        ctsSnPham.setTrangThai(trangThai);
        chiTietSanPhamRes.save(ctsSnPham);
    }

    public void deleteSanPhamReal(Long idsp, int trangThai) {
        SanPham SnPham = resSanPham.findById(idsp).get();
        SnPham.setTrangThai(trangThai);
        resSanPham.save(SnPham);
    }

    public List showSanPhamDistinctDiscount() {
        return resSanPham.findAll();
    }

    @Override
    public SanPham searchSanPhamBy(String name, Long idsp) {
        Optional<SanPham> optional = resSanPham.findSanPhamByAll(idsp, name);
//        if (!optional.isPresent())
//            throw new SanPhamNotFoundException(Const.SP_NOT_FOUND);
//        SanPham sanPham = optional.get();
//        DTOSanPhamRespon dtoSanPhamRespon = MapperUtils.dtoToEntity(optional.get(), DTOSanPhamRespon.class);
//        if (sanPham.getDanhMuc() != null)
//        dtoSanPhamRespon.set_tenDanhMuc(sanPham.getDanhMuc().getNameDanhMuc());
//        dtoSanPhamRespon.set_tenThuongHieu(sanPham.getThuongHieu().getTen());
//        dtoSanPhamRespon.set_trongLuong(sanPham.getSanPhamChiTietList().stream().collect(Collectors.toSet())
//                .stream().map(i -> i.getTrongLuong().getDonVi()).collect(Collectors.toSet()));
//        return dtoSanPhamRespon;
        return optional.get();
    }
}