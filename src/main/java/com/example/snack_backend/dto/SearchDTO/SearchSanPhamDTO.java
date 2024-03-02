package com.example.snack_backend.dto.SearchDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SearchSanPhamDTO {
    private String name;
    private Long idsp;
}
