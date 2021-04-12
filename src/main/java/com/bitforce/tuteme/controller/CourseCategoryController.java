package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.service.CourseCategoryService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Map<String, Object>> getAllCourseCategory(@PathVariable int page){
        return courseCategoryService.getAllCourseCategory(page);
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
