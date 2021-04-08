package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.repository.CourseCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;
    private final FileStorageService fileStorageService = new FileStorageService("CourseCategory");

    public  CourseCategory createCategory(MultipartFile file,CourseCategory courseCategory) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/category/uploads/courseCategory/")
                .path(fileName)
                .toUriString();

        CourseCategory courseCategory1 = new CourseCategory();
        courseCategory1.setImageUrl(fileDownloadUri);
        courseCategory1.setCategory(courseCategory.getCategory());

        return courseCategoryRepository.save(courseCategory1);
    }

    public Page<CourseCategory> getAllCourseCategory(Pageable pageable) {
        return courseCategoryRepository.findAll(pageable);
    }

    public Optional<CourseCategory> getCourseCategory(Long categoryId) {
        return courseCategoryRepository.findById(categoryId);
    }

    public String deleteCourseCategory(Long categoryId) {
        courseCategoryRepository.deleteById(categoryId);
        return "CourseCategory Deleted Successfully..!";
    }


    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename,request);
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }
}
