package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ServiceResponse.GetCourseCategoryResponse;
import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.service.CourseCategoryService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/courses/category")
public class CourseCategoryController {

   private final CourseCategoryService courseCategoryService;

    @PostMapping
    public CourseCategory createCategory(@RequestParam MultipartFile file , @ModelAttribute CourseCategory courseCategory){
        return courseCategoryService.createCategory(file,courseCategory);
    }

    @GetMapping("/getAll/{page}")
    public GetCourseCategoryResponse getAllCourseCategory(@PathVariable int page){
        try {
            return courseCategoryService.getAllCourseCategory(page);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{categoryId}")
    public Optional<CourseCategory> getCourseCategory(@PathVariable Long categoryId){
        return courseCategoryService.getCourseCategory(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public String deleteCourseCategory(@PathVariable Long categoryId){
        return courseCategoryService.deleteCourseCategory(categoryId);
    }

    @SneakyThrows
    @GetMapping(value = "uploads/courseCategory/{filename}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageResource(@PathVariable String filename) {
        return courseCategoryService.getImageByte(filename);
    }

}
