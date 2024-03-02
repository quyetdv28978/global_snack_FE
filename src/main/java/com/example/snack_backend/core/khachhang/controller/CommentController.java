package com.example.snack_backend.core.khachhang.controller;


import com.example.snack_backend.core.admin.repository.AdUserRepository;
import com.example.snack_backend.core.khachhang.model.request.CommentRequest;
import com.example.snack_backend.core.khachhang.service.KHCommentService;
import com.example.snack_backend.core.token.service.TokenService;
import com.example.snack_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/khach-hang/comment")
public class CommentController {

    @Autowired
    KHCommentService khCommentService;

    @Autowired
    TokenService tokenService;

    @Autowired
    AdUserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody CommentRequest request, @RequestParam String token) {
        return ResponseEntity.ok(khCommentService.addComment(request,token));
    }

    @PostMapping("/addPhanHoi")
    public ResponseEntity<?> addPhanHoi(@RequestBody CommentRequest request, @RequestParam String token) {
        return ResponseEntity.ok(khCommentService.addPhanHoi(request,token));
    }

    @GetMapping()
    public ResponseEntity<?> getList (@RequestParam("idsp") Integer idsp ) {

        return ResponseEntity.ok(khCommentService.getListComment(idsp));
    }

    @GetMapping("/getList")
    public ResponseEntity<?> getListById () {

        return ResponseEntity.ok(khCommentService.getListCommentByIdPhanHoi());
    }

    @DeleteMapping("/{idcomment}")
    public void deleteGHCT(@PathVariable(value = "idcomment") Integer idcomment) {

        khCommentService.deleteComment(idcomment);
    }

}
