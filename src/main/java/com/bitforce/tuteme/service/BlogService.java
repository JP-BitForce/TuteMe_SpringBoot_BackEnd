package com.bitforce.tuteme.service;

import com.bitforce.tuteme.PageableEntity.PageableCoreBlogs;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewBlogRequest;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.BlogRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {
    private final Logger log = LoggerFactory.getLogger(BlogService.class);

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService = new FileStorageService("Blogs");
    private final StudentProfileService studentProfileService;
    private final TutorProfileService tutorProfileService;

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

    public PageableCoreBlogs getAllBlogsForUser(Long userId,int page) {
        Page<Blog> blogPage = blogRepository.findAllByUserId(
                userId,
                PageRequest.of(page, 10)
        );
        return new PageableCoreBlogs(
                blogPage.get()
                        .map(this::convertToCoreEntity)
                        .collect(Collectors.toList()),
                blogPage.getTotalPages(),
                blogPage.getNumber()
        );
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

    @SneakyThrows
    public byte[] getBlogImageByte(String url){
        if(url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/blog/uploads/Blogs/");
            return fileStorageService.convert(filename[1]);
        } else {
            return null;
        }
    }

    @SneakyThrows
    public byte[] getBlogAuthorImageByte(Long authorId) {
        boolean exist = userRepository.findById(authorId).isPresent();
        if (!exist) {
            log.error("user not found for id: {}", authorId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(authorId).get();
        String imgUrl = user.getImageUrl();
        if (imgUrl != null) {
            String[] filename;
            if (user.getType().equals("student")) {
                filename = imgUrl.trim().split("http://localhost:8080/api/student/profile/uploads/profilePicture/student/");
                return studentProfileService.getImageByte(filename[1]);
            } else {
                filename = imgUrl.trim().split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
                return tutorProfileService.getImageByte(filename[1]);
            }
        } else {
            return null;
        }
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
