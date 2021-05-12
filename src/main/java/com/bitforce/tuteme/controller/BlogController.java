package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.PageableEntity.PageableCoreBlogs;
import com.bitforce.tuteme.dto.ControllerRequest.AddNewBlogControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.GetBlogsControllerRequest;
import com.bitforce.tuteme.dto.ControllerResponse.GetBlogsControllerResponse;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewBlogRequest;
import com.bitforce.tuteme.model.Blog;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService blogService;

    @PostMapping(value = "/addNew", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Blog createBlog(@RequestPart("blog") AddNewBlogControllerRequest request, @RequestPart("file") MultipartFile file) {
        try {
            AddNewBlogRequest addNewBlogRequest = new AddNewBlogRequest(
                    request.getUserId(),
                    request.getTitle(),
                    request.getDescription(),
                    request.getContent(),
                    file
            );
            return blogService.CreateBlog(addNewBlogRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getAll/{page}")
    public ResponseEntity<?> getAllBlogs(@PathVariable int page) {
        try {
            PageableCoreBlogs pageableCoreBlogs = blogService.getAllBlogs(page);
            GetBlogsControllerResponse response = getResponse(pageableCoreBlogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{blogId}")
    public Blog getBlog(@PathVariable Long blogId) {
        return blogService.getBlog(blogId);
    }

    @PostMapping("/getUserBlogs")
    public ResponseEntity<?> getAllBlogsForUser(@RequestBody GetBlogsControllerRequest request) {
        try {
            PageableCoreBlogs pageableCoreBlogs = blogService.getAllBlogsForUser(
                    request.getProfileId(),
                    request.getPage(),
                    request.getRole()
            );
            GetBlogsControllerResponse response = getResponse(pageableCoreBlogs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{blogId}")
    public Blog updateBlog(@RequestBody Blog blog, @PathVariable Long blogId) {
        return blogService.updateBlog(blog, blogId);
    }

    @DeleteMapping("/{blogId}")
    public String deleteBlog(@PathVariable Long blogId) {
        return blogService.deleteBlog(blogId);
    }

    @PutMapping("/like/{blogId}")
    public Blog doLike(@PathVariable Long blogId) {
        return blogService.doLike(blogId);
    }

    @PutMapping("/unlike/{blogId}")
    public Blog doUnLike(@PathVariable Long blogId) {
        return blogService.doUnLike(blogId);
    }

    public GetBlogsControllerResponse getResponse(PageableCoreBlogs pageableCoreBlogs) {
        return new GetBlogsControllerResponse(
                pageableCoreBlogs.getBlog().stream().map(blog -> new GetBlogsControllerResponse.Blog(
                                blog.getId(),
                                blog.getTitle(),
                                blog.getContent(),
                                blog.getLikes(),
                                blog.getDate(),
                                blog.getDescription(),
                                blogService.getBlogImageByte(blog.getCoverImgUrl()),
                                blog.getComments(),
                                blog.getUser().getId().toString(),
                                blogService.getBlogAuthorImageByte(blog.getUser().getId()),
                                getAuthorName(blog.getUser())
                        )
                ).collect(Collectors.toList()),
                pageableCoreBlogs.getTotal(),
                pageableCoreBlogs.getCurrent()
        );
    }

    public String getAuthorName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
