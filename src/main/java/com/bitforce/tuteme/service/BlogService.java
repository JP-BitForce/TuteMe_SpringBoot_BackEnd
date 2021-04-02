package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.repository.BlogRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Blog CreateBlog(Blog newBlog) {
        Blog blog = new Blog();
        blog.setTitle(newBlog.getTitle());
        blog.setContent(newBlog.getContent());
        blog.setDate(LocalDateTime.now());
        blog.setLikes(0);
        blog.setUser(newBlog.getUser());
       return blogRepository.save(blog);
    }

    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public Blog getBlog(Long blogId) {
        return blogRepository.findById(blogId).get();
    }

    public Page<Blog> getAllBlogsForUser(Long userId,Pageable pageable) {
        return blogRepository.findAllByUserId(userId,pageable);
    }

    public Blog updateBlog(Blog blog, Long blogId) {
        Blog blog1 =blogRepository.findById(blogId).get();
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setDate(LocalDateTime.now());
        return blogRepository.save(blog1);
    }

    public String deleteBlog(Long blogId) {
        blogRepository.deleteById(blogId);
        return "Blog Deleted Successfully..";
    }

    public Blog doLike(Long blogId) {
        Blog blog = blogRepository.findById(blogId).get();
        blog.setLikes(blog.getLikes()+1);
        return blogRepository.save(blog);
    }

    public Blog doUnLike(Long blogId) {
        Blog blog = blogRepository.findById(blogId).get();
        blog.setLikes(blog.getLikes()-1);
        return blogRepository.save(blog);
    }
}
