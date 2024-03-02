package com.example.snack_backend.core.admin.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminAddImageRequest {
    private Integer idSP;

    private String anh;
}
