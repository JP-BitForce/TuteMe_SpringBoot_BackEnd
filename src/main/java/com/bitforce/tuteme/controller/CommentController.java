package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.Comment;
import com.bitforce.tuteme.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/blog/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public Comment addComment(@RequestBody Comment comment,@RequestParam Long blogId){
        return commentService.addComment(comment,blogId);
    }

    @PutMapping("/{commentId}")
    public Comment editComment(@RequestBody Comment comment,@PathVariable Long commentId){
        return commentService.editComment(comment,commentId);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long blogId){
        return commentService.deleteComment(commentId,blogId);
    }
}
