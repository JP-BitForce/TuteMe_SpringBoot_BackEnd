package com.bitforce.tuteme.service;

import com.bitforce.tuteme.PageableEntity.PageableCoreBlogs;
import com.bitforce.tuteme.dto.ControllerResponse.GetBlogsControllerResponse;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewBlogRequest;
import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.BlogRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService = new FileStorageService("Blogs");

    public Blog CreateBlog(AddNewBlogRequest request) {
        String fileName = fileStorageService.storeFile(request.getFile());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/blog/uploads/Blogs/")
                .path(fileName)
                .toUriString();

        User user = userRepository.findById(Long.parseLong(request.getUserId())).get();


        Blog blog = new Blog(
                request.getTitle(),
                request.getContent(),
                0,
                LocalDateTime.now(),
                request.getDescription(),
                fileDownloadUri,
                user
        );
        return blogRepository.save(blog);
    }

    public PageableCoreBlogs getAllBlogs(int page) {
        Page<Blog> blogPage = blogRepository.findAll(PageRequest.of(page, 10));
        return new PageableCoreBlogs(
                blogPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                blogPage.getTotalPages(),
                blogPage.getNumber()
        );
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

    public Blog convertToCoreEntity(Blog blog) {
        return new Blog(
          blog.getId(),
          blog.getTitle(),
          blog.getContent(),
          blog.getLikes(),
          blog.getDate(),
          blog.getDescription(),
          blog.getCoverImgUrl(),
          blog.getComments(),
          blog.getUser()
        );
    }
}
