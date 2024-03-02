package com.example.snack_backend.infrastructure.adapter;

public interface EntityToDto<Entity, DTO> {
    public DTO changeToDto(Entity entity);
}