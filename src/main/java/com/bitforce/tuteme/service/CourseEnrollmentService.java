package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.CourseDTO;
import com.bitforce.tuteme.dto.CourseEnrollmentDTO;
import com.bitforce.tuteme.model.Course;
import com.bitforce.tuteme.model.CourseEnrollment;
import com.bitforce.tuteme.repository.CourseEnrollmentRepository;
import com.bitforce.tuteme.repository.CourseRepository;
import com.bitforce.tuteme.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseEnrollment enrollToCourse(Long courseId, Long studentId) {
        CourseEnrollment courseEnrollment = new CourseEnrollment();
        courseEnrollment.setCourse(courseRepository.findById(courseId).get());
        courseEnrollment.setStudent(studentRepository.findById(studentId).get());
        courseEnrollmentRepository.save(courseEnrollment);
        return courseEnrollment;
    }

    public ResponseEntity<Map<String, Object>> getAllEnrolledCoursesForStudent(Long studentId, int page) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<CourseEnrollment> coursePage = courseEnrollmentRepository.findAllByStudentId(studentId,paging);
            List<CourseEnrollment> courseEnrollments = coursePage.getContent();

            List<CourseEnrollmentDTO> courseEnrollmentDTOS = new ArrayList<>();
            for (CourseEnrollment courseEnrollment : courseEnrollments){
                CourseEnrollmentDTO courseEnrollmentDTO = new CourseEnrollmentDTO();
                courseEnrollmentDTO.setId(courseEnrollment.getId());
                Course course =courseEnrollment.getCourse();
                CourseDTO courseDTO = new CourseDTO();
                BeanUtils.copyProperties(course,courseDTO);
                courseDTO.setCategoryId(course.getCourseCategory().getId());
                courseDTO.setCategoryName(course.getCourseCategory().getCategory());
                courseDTO.setTutorId(course.getTutor().getId());
                courseDTO.setTutorName(course.getTutor().getUser().getFirstName() +" "+course.getTutor().getUser().getLastName());
                courseEnrollmentDTO.setCourse(courseDTO);

                courseEnrollmentDTOS.add(courseEnrollmentDTO);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", courseEnrollmentDTOS);
            response.put("current", coursePage.getNumber());
            response.put("total", coursePage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String unEnrollToCourse(Long enrollmentId) {
        courseEnrollmentRepository.deleteById(enrollmentId);
        return "Course UnEnrolled Successfully...!";
    }
}
