package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(path = "dia-chi")
public interface DiaChiReponsitory extends JpaRepository<DiaChi, Integer> {
}
