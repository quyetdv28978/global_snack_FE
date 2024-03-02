package com.example.snack_backend.core.khachhang.service.impl;



import com.example.snack_backend.core.admin.repository.AdUserRepository;
import com.example.snack_backend.core.khachhang.model.request.CommentRequest;
import com.example.snack_backend.core.khachhang.repository.KHCommentRepository;
import com.example.snack_backend.core.khachhang.service.KHCommentService;
import com.example.snack_backend.core.token.service.TokenService;
import com.example.snack_backend.entity.Comment;
import com.example.snack_backend.entity.SanPham;
import com.example.snack_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KHCommentServiceImpl implements KHCommentService {

    @Autowired
    KHCommentRepository khCommentRepo;

    @Autowired
    TokenService tokenService;

    @Autowired
    AdUserRepository userRepository;


    @Override
    public Comment addComment(CommentRequest request, String token) {

        if (tokenService.getUserNameByToken(token) == null) {
            return null;
        }

        String userName = tokenService.getUserNameByToken(token);
        User user = userRepository.findByUserName(userName);

        Comment comment = new Comment();
        comment.setNoiDung(request.getNoiDung());
        comment.setUser(user);
        comment.setSanPham(SanPham.builder().id(request.getSanPham()).build());


        return khCommentRepo.save(comment);
    }

    @Override
    public Comment addPhanHoi(CommentRequest request, String token) {
        if (tokenService.getUserNameByToken(token) == null) {
            return null;
        }

        String userName = tokenService.getUserNameByToken(token);
        User user = userRepository.findByUserName(userName);

        Comment comment = new Comment();
        comment.setNoiDung(request.getNoiDung());
        comment.setIdPhanHoi(request.getIdPhanHoi());
        comment.setUser(user);
        comment.setSanPham(SanPham.builder().id(request.getSanPham()).build());

        return khCommentRepo.save(comment);
    }

    @Override
    public List<Comment> getListComment(Integer idsp) {
        return khCommentRepo.getListComment( idsp);
    }

    @Override
    public List<Comment> getListCommentByIdPhanHoi() {
        return khCommentRepo.getListCommentByIdPhanHoi();
    }

    @Override
    public void deleteComment(Integer id) {

        Comment comment = khCommentRepo.findById(id).get();

        List<Comment> listcomment = khCommentRepo.getListByIdPhanHoi(id);
        for (Comment cm : listcomment) {
            khCommentRepo.deleteById(cm.getId());
        }
        khCommentRepo.deleteById(comment.getId());
        // }
    }

}
