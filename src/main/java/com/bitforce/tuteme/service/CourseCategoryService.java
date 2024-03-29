package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceResponse.GetCourseCategoryResponse;
import com.bitforce.tuteme.model.CourseCategory;
import com.bitforce.tuteme.repository.CourseCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;
    private final FileStorageService fileStorageService = new FileStorageService("CourseCategory");

    public CourseCategory createCategory(MultipartFile file, CourseCategory courseCategory) {
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

    public GetCourseCategoryResponse getAllCourseCategory(int page) {
        Pageable paging = PageRequest.of(page, 10);
        Page<CourseCategory> coursePage = courseCategoryRepository.findAll(paging);
        List<CourseCategory> courseCategories = coursePage.getContent();

        return new GetCourseCategoryResponse(
                courseCategories.stream().map(courseCategory -> new GetCourseCategoryResponse.CourseCategory(
                                courseCategory.getId(),
                                courseCategory.getCategory(),
                                getImageSource(courseCategory.getImageUrl())
                        )
                ).collect(Collectors.toList()),
                coursePage.getTotalPages(),
                coursePage.getNumber()
        );
    }

    public Optional<CourseCategory> getCourseCategory(Long categoryId) {
        return courseCategoryRepository.findById(categoryId);
    }

    public String deleteCourseCategory(Long categoryId) {
        courseCategoryRepository.deleteById(categoryId);
        return "CourseCategory Deleted Successfully..!";
    }

    public List<CourseCategory> getCourseCategoryByName(List<String> categories) {
        return courseCategoryRepository.findAllByCategoryIn(categories);
    }


    public ResponseEntity<Resource> getImageResource(String filename, HttpServletRequest request) {
        return fileStorageService.loadFileAsResource(filename, request);
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    public List<CourseCategory> getCourseCategories() {
        return courseCategoryRepository.findAll();
    }

    @SneakyThrows
    public byte[] getImageSource(String url) {
        if (url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/courses/category/uploads/courseCategory/");
            return getImageByte(filename[1]);
        }
        return null;
    }

    public CourseCategory getCategoryByName(String category) {
        return courseCategoryRepository.findByCategory(category);
    }
}
