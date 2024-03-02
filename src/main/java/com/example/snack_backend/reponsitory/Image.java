package com.example.snack_backend.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Image extends JpaRepository<com.example.snack_backend.entity.Image, Long> {
}
