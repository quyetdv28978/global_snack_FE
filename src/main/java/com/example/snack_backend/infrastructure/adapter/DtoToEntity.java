package com.example.snack_backend.infrastructure.adapter;

public interface DtoToEntity<ENTITY> {
    ENTITY dtoToEntity(ENTITY e);
}
