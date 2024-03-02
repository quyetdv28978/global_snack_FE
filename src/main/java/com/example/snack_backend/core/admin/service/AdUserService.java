package com.example.snack_backend.core.admin.service;


import com.example.snack_backend.core.admin.model.request.AdminDiaChiRequest;
import com.example.snack_backend.core.admin.model.request.AdminUserRequest;
import com.example.snack_backend.core.admin.model.response.AdminHoaDonResponse;
import com.example.snack_backend.core.admin.model.response.AdminUserResponse;
import com.example.snack_backend.entity.DiaChi;
import com.example.snack_backend.entity.User;

import java.util.List;

public interface AdUserService {
    List<AdminUserResponse> getKhachHang();

    List<AdminUserResponse> getNhanVien();

    List<AdminUserResponse> getAdmin();

    List<User> getAllByTrangThai(Integer trangThai);

    List<DiaChi> getUserByDiaChi(Integer idUser);

    AdminUserResponse add(AdminUserRequest user);

    AdminUserResponse delete(Integer id);

    AdminUserResponse update(AdminUserRequest user, Integer id);

    AdminUserResponse VoHieuHoaUser(Integer id);

    AdminHoaDonResponse getHoaDonByIdUser(Integer id);

    List<AdminUserResponse> getAllUserByRole(String role);

    List<AdminUserResponse> getAllUser();

    DiaChi addDiaChi(AdminDiaChiRequest request);
}
