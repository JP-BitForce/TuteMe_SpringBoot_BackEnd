package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ControllerRequest.AddCommentControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.AddCommentReplyControllerRequest;
import com.bitforce.tuteme.dto.ControllerResponse.GetCommentsControllerResponse;
import com.bitforce.tuteme.dto.ServiceRequest.AddCommentReplyRequest;
import com.bitforce.tuteme.dto.ServiceResponse.ReplyResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Comment;
import com.bitforce.tuteme.model.CommentReply;
import com.bitforce.tuteme.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/blog/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("addComment")
    public Comment addComment(@RequestBody AddCommentControllerRequest request) {
        try {
            return commentService.addComment(
                    request.getUserId(),
                    request.getBlogId(),
                    request.getComment()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public Comment editComment(@RequestBody Comment comment,@PathVariable Long commentId){
        return commentService.editComment(comment,commentId);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long blogId){
        return commentService.deleteComment(commentId,blogId);
    }

    @GetMapping("/getComments/{blogId}")
    public ResponseEntity<?> getComments(@PathVariable String blogId) {
        try {
            GetCommentsControllerResponse response = commentService.getComments(Long.parseLong(blogId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("addCommentReply")
    public ReplyResponse addReply(@RequestBody AddCommentReplyControllerRequest request) {
        try {
            AddCommentReplyRequest addCommentReplyRequest = new AddCommentReplyRequest(
                    request.getUserId(),
                    request.getBlogId(),
                    request.getCommentParentId(),
                    request.getReply()
            );
            return commentService.addCommentReply(addCommentReplyRequest);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
