package com.example.snack_backend.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Const {
    public final static class SEND_MAIL_SUBJECT {
        public final static String CLIENT_REGISTER = "XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG";
    }

    public final static class TEMPLATE_FILE_NAME {
        public final static String CLIENT_REGISTER = "client";
    }

    public final static class SEND_MAIL_OTP {
        public final static String CLIENT_REGISTER = "XÁC NHẬN MÃ OTP";
    }

    public final static class TEMPLATE_OTP_NAME {
        public final static String CLIENT_REGISTER = "otp";
    }

    public final static String USER_NOT_FOUND = "User not found";
    public final static String ROLE_NOT_FOUND = "Role not found";
    public final static String DELETE_SUSSCESS = "Delete susscess";
    public final static String SUSSCESS = "susscess";

    public final static String AUTHORIZATION = "You don't have access this resource";

    public final static String SP_NOT_FOUND = "Product not found";
    public final static String KH_NOT_FOUND = "Customer not found";
    public final static String Danh_Muc_NOT_FOUND = "category not found";
    public final static String Dia_Chi_NOT_FOUND = "Dia chi not found";

    public final static String PAYMENT_ERROR = "Thanh Toan that bai";

    public static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources");
    public final static String FILE_FORMAT = "file -> jpg, png";

    public final static String NOTICE_MAIL_OLD_DATE = "";

    public final static Long H_MS = 3600 * 1000L;
}
