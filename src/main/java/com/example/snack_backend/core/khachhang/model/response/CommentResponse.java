package com.example.snack_backend.core.khachhang.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


public interface CommentResponse {

    @Value("#{target.id}")
    Integer getId();

    @Value("#{target.noiDung}")
    String getNoiDung();

    @Value("#{target.tenSP}")
    String getTenSP();

    @Value("#{target.tenUser}")
    String getTenUser();


}
