package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ControllerResponse.GetCommentsControllerResponse;
import com.bitforce.tuteme.dto.ServiceRequest.AddCommentReplyRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.model.Comment;
import com.bitforce.tuteme.model.CommentReply;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.BlogRepository;
import com.bitforce.tuteme.repository.CommentReplyRepository;
import com.bitforce.tuteme.repository.CommentRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final BlogService blogService;
    private final BlogRepository blogRepository;
    private final StudentProfileService studentProfileService;
    private final TutorProfileService tutorProfileService;
    private final UserRepository userRepository;
    private final CommentReplyRepository commentReplyRepository;

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

    public void addCommentReply(AddCommentReplyRequest request) throws EntityNotFoundException {
        Long userId = Long.parseLong(request.getUserId());
        if (!userRepository.findById(userId).isPresent()) {
            log.error("User not found id: {}", userId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userId).get();

        Long blogId = Long.parseLong(request.getBlogId());
        Blog blog = blogService.getBlog(blogId);
        if (blog == null) {
            log.error("Blog not found id: {}", blogId);
            throw new EntityNotFoundException("BLOG_NOT_FOUND");
        }

        Long commentId = Long.parseLong(request.getCommentParentId());
        if (!commentRepository.findById(commentId).isPresent()) {
            log.error("Comment not found id: {}", commentId);
            throw new EntityNotFoundException("COMMENT_NOT_FOUND");
        }
        Comment comment = commentRepository.findById(commentId).get();

        CommentReply commentReplyEntity = CommentReply
                .builder()
                .reply(request.getReply())
                .date(LocalDateTime.now())
                .user(user)
                .build();
        CommentReply commentReply = commentReplyRepository.save(commentReplyEntity);
        List<CommentReply> commentReplyList = new ArrayList<>();
        commentReplyList.add(commentReply);
        comment.setCommentReply(commentReplyList);
    }

    public GetCommentsControllerResponse getComments(Long blogId) throws EntityNotFoundException {
        Blog blog = blogService.getBlog(blogId);
        if (blog == null) {
            log.error("Blog not found id: {}", blogId);
            throw new EntityNotFoundException("BLOG_NOT_FOUND");
        }
        return getCommentsControllerResponse(blog.getComments(), blogId);
    }

    public GetCommentsControllerResponse getCommentsControllerResponse(List<Comment> comments, Long blogId) {
        return new GetCommentsControllerResponse(
                comments.stream().map(comment -> new GetCommentsControllerResponse.Comment(
                        comment.getId(),
                        blogId,
                        comment.getUser().getId(),
                        getAuthorName(comment.getUser()),
                        comment.getBlogComment(),
                        comment.getDate(),
                        getAuthorImgSource(comment.getUser()),
                        comment.getCommentReply().stream().map(reply -> new GetCommentsControllerResponse.Comment.Reply(
                                reply.getId(),
                                reply.getUser().getId(),
                                getAuthorName(reply.getUser()),
                                reply.getReply(),
                                reply.getDate(),
                                getAuthorImgSource(reply.getUser())
                        )).collect(Collectors.toList())
                )).collect(Collectors.toList())
        );
    }

    public String getAuthorName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @SneakyThrows
    public byte[] getAuthorImgSource(User user) {
        if (user.getType().equals("student")) {
            String[] filename = user
                    .getImageUrl()
                    .trim()
                    .split("http://localhost:8080/api/student/profile/uploads/profilePicture/student/");
            return studentProfileService.getImageByte(filename[1]);
        } else {
            String[] filename = user
                    .getImageUrl()
                    .trim()
                    .split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
            return tutorProfileService.getImageByte(filename[1]);
        }
    }
}
