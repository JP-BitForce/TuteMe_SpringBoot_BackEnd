package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.model.Comment;
import com.bitforce.tuteme.repository.BlogRepository;
import com.bitforce.tuteme.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogService blogService;
    private final BlogRepository blogRepository;

    public Comment addComment(Comment newComment,Long blogId) {
        Comment comment = new Comment();
        comment.setBlogComment(newComment.getBlogComment());
        comment.setDate(LocalDateTime.now());
        comment.setUser(newComment.getUser());
       Comment savedComment= commentRepository.save(comment);

        Blog blog =blogRepository.findById(blogId).get();
        List<Comment> comments =blog.getComments();
        comments.add(savedComment);
        blog.setComments(comments);
        blogRepository.save(blog);
        return comment;
    }

    public Comment editComment(Comment comment, Long commentId) {
        Comment comment1 = commentRepository.findById(commentId).get();
        comment1.setBlogComment(comment.getBlogComment());
        comment1.setDate(LocalDateTime.now());
        commentRepository.save(comment1);
        return comment1;
    }

    public String deleteComment(Long commentId, Long blogId) {
        Comment comment = commentRepository.findById(commentId).get();
        Blog blog = blogRepository.findById(blogId).get();
        List<Comment> comments = blog.getComments();
        comments.remove(comment);
        blog.setComments(comments);
        blogRepository.save(blog);
        commentRepository.deleteById(commentId);
        return "Comment deleted successfully....";
    }
}
