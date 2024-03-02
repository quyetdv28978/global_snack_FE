package com.example.snack_backend.reponsitory;

import com.example.snack_backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


}
