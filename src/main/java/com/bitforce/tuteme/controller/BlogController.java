package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService blogService;

    @PostMapping
    public Blog createBlog(@RequestBody Blog blog){
        return blogService.CreateBlog(blog);
    }

    @GetMapping
    public Page<Blog> getAllBlogs(Pageable pageable){
        return blogService.getAllBlogs(pageable);
    }

    @GetMapping("/{blogId}")
    public Blog getBlog(@PathVariable Long blogId){
        return blogService.getBlog(blogId);
    }

    @GetMapping("/{userId}")
    public Page<Blog> getAllBlogsForUser(@PathVariable Long userId,Pageable pageable){
        return blogService.getAllBlogsForUser(userId,pageable);
    }

    @PutMapping("/{blogId}")
    public Blog updateBlog(@RequestBody Blog blog, @PathVariable Long blogId){
        return blogService.updateBlog(blog,blogId);
    }

    @DeleteMapping("/{blogId}")
    public String deleteBlog(@PathVariable Long blogId){
        return blogService.deleteBlog(blogId);
    }

    @PutMapping("/like/{blogId}")
    public Blog doLike(@PathVariable Long blogId){
        return blogService.doLike(blogId);
    }
    @PutMapping("/unlike/{blogId}")
    public Blog doUnLike(@PathVariable Long blogId){
        return blogService.doUnLike(blogId);
    }


}
