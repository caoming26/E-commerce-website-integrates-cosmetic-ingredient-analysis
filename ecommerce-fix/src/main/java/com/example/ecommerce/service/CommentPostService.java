package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.InternalServerException;
import com.example.ecommerce.exception.NotFoundException;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class CommentPostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private UserRepository userRepository;

    public CommentPost createComment(CommentPost comment, Long postId, Long userId) {

        Post post = postRepository.findById(postId).get();

        User user = userRepository.getReferenceById(userId);

        comment.setPost(post);

        comment.setUser(user);

        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        commentPostRepository.save(comment);

        return comment;
    };

    public PageUtil<CommentPost> getAll(int page, int size) {
        Page<CommentPost> pageInfo = commentPostRepository.findAll(PageRequest.of(page - 1, size));
        return new PageUtil<>(
                pageInfo.getContent(),
                pageInfo.getPageable().getPageNumber() + 1,
                pageInfo.getPageable().getPageSize(),
                IntStream.range(1, pageInfo.getTotalPages() + 1).boxed().toList(),
                pageInfo.getTotalElements(),
                pageInfo.isFirst(),
                pageInfo.isLast()
        );
    }

    public void deleteComment(long id) {
        Optional<CommentPost> commentPostOptional = commentPostRepository.findById(id);
        if (commentPostOptional.isEmpty()) {
            throw new NotFoundException("Comment không tồn tại");
        }
        try {
            commentPostRepository.delete(commentPostOptional.get());
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa bình luận");
        }
    }

    public List<User> getUsersWhoCommented() {
        List<User> users = commentPostRepository.findDistinctUsers();
        return users;
    }
}
