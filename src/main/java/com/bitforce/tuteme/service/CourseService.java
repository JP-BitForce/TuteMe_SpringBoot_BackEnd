package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ControllerRequest.UpdateCourseControllerRequest;
import com.bitforce.tuteme.dto.CourseDTO;
import com.bitforce.tuteme.dto.CourseTutorDTO;
import com.bitforce.tuteme.dto.ServiceRequest.CreateNewCourseRequest;
import com.bitforce.tuteme.dto.ServiceRequest.FilterCoursesRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetCourseByIdResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetCourseByTutorResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetFilterCategoriesResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final FileStorageService fileStorageService = new FileStorageService("Courses");
    private final CourseCategoryService courseCategoryService;
    private final CourseLevelRespository courseLevelRespository;
    private final CoursePriceCategoryRepository coursePriceCategoryRepository;
    private final TutorProfileService tutorProfileService;
    private final CourseTypeRepository courseTypeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseDurationRepository courseDurationRepository;
    private final ScheduleRepository scheduleRepository;

    public CourseService(CourseRepository courseRepository,
                         CourseCategoryService courseCategoryService,
                         CourseLevelRespository courseLevelRespository,
                         CoursePriceCategoryRepository coursePriceCategoryRepository,
                         TutorProfileService tutorProfileService,
                         CourseTypeRepository courseTypeRepository,
                         EnrollmentRepository enrollmentRepository,
                         UserRepository userRepository,
                         CourseDurationRepository courseDurationRepository,
                         ScheduleRepository scheduleRepository) {
        this.courseRepository = courseRepository;
        this.courseCategoryService = courseCategoryService;
        this.courseLevelRespository = courseLevelRespository;
        this.coursePriceCategoryRepository = coursePriceCategoryRepository;
        this.tutorProfileService = tutorProfileService;
        this.courseTypeRepository = courseTypeRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseDurationRepository = courseDurationRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public GetCourseByTutorResponse createCourse(MultipartFile file, CreateNewCourseRequest request) throws EntityNotFoundException {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/uploads/Courses/")
                .path(fileName)
                .toUriString();

        CourseCategory courseCategory = courseCategoryService.getCategoryByName(request.getCategory());
        CourseType courseType = courseTypeRepository.findByTitle(request.getType());
        List<CoursePriceCategory> coursePriceCategories = coursePriceCategoryRepository.findAll();
        CoursePriceCategory coursePriceCategory = null;
        if (!coursePriceCategories.isEmpty()) {
            for (CoursePriceCategory category : coursePriceCategories) {
                int minComparison = request.getPrice().compareTo(category.getPriceMin());
                int maxComparison = request.getPrice().compareTo(category.getPriceMax());
                if (minComparison >= 0 && maxComparison < 0) {
                    coursePriceCategory = category;
                }
            }
        }

        Tutor tutor = tutorProfileService.getTutor(request.getTutorId());
        CourseDuration duration = CourseDuration
                .builder()
                .year(request.getYear())
                .month(request.getMonth())
                .days(request.getDays())
                .build();
        CourseDuration courseDuration = courseDurationRepository.save(duration);

        List<Schedule> schedules = saveAndGetSchedules(request.getSchedules());

        Course course = new Course(
                request.getCourseName(),
                request.getDescription(),
                fileDownloadUri,
                (double) 5,
                request.getYear() + " years" + " " + request.getMonth() + " months" + " " + request.getDays() + " days",
                request.getPrice(),
                tutor,
                courseCategory,
                coursePriceCategory,
                courseType,
                schedules,
                courseDuration
        );
        Course persisted = courseRepository.save(course);
        return getCourseByTutorResponse(persisted);
    }

    public ResponseEntity<Map<String, Object>> getAllCourses(int page, Long userId) {
        try {
            Pageable paging = PageRequest.of(page, 10);
            Page<Course> coursePage = courseRepository.findAll(paging);
            List<Course> courses = coursePage.getContent();

            List<CourseDTO> courseDTOS = new ArrayList<>();
            for (Course course : courses) {
                CourseDTO courseDTO = new CourseDTO();
                BeanUtils.copyProperties(course, courseDTO);
                courseDTO.setCategoryId(course.getCourseCategory().getId());
                courseDTO.setCategoryName(course.getCourseCategory().getCategory());
                courseDTO.setTutorId(course.getTutor().getId());
                courseDTO.setTutorName(course.getTutor().getUser().getFirstName() + " " + course.getTutor().getUser().getLastName());
                courseDTO.setImageUrl(getCourseImage(course.getImageUrl()));
                courseDTO.setEnrolledByCurrentUser(isCurrentUserEnrolled(userId, course));
                courseDTO.setSchedules(course.getSchedules());
                courseDTOS.add(courseDTO);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", courseDTOS);
            response.put("current", coursePage.getNumber());
            response.put("total", coursePage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public GetCourseByTutorResponse updateCourse(UpdateCourseControllerRequest request, MultipartFile file) throws EntityNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(request.getCourseId());
        if (!courseOptional.isPresent()) {
            throw new EntityNotFoundException("COURSE_NOT_FOUND");
        }
        Course course = courseOptional.get();

        if (file != null) {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/courses/uploads/Courses/")
                    .path(fileStorageService.storeFile(file))
                    .toUriString();
            course.setImageUrl(fileDownloadUri);
        }

        List<Schedule> schedules = saveAndGetSchedules(request.getSchedules());
        CourseCategory courseCategory = getCourseCategoryByName(request.getCategory());
        CourseType courseType = getCourseTypeByName(request.getType());
        CoursePriceCategory coursePriceCategory = getCoursePriceCategory(request.getPrice());

        CourseDuration duration = course.getCourseDuration();
        duration.setYear(request.getYear());
        duration.setMonth(request.getMonth());
        duration.setDays(request.getDays());
        CourseDuration courseDuration = courseDurationRepository.save(duration);

        course.setName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCourseCategory(courseCategory);
        course.setCourseType(courseType);
        course.setCoursePriceCategory(coursePriceCategory);
        course.setSchedules(schedules);
        course.setDuration(request.getYear() + " years" + " " + request.getMonth() + " months" + " " + request.getDays() + " days");
        course.setCourseDuration(courseDuration);
        courseRepository.save(course);
        return getCourseByTutorResponse(course);
    }

    public String deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        return "CourseCategory Deleted Successfully..!";
    }

    public List<CourseTutorDTO> getAllTutors() {
        List<CourseTutorDTO> courseTutorDTOS = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            CourseTutorDTO courseTutorDTO = new CourseTutorDTO();
            courseTutorDTO.setId(course.getTutor().getId());
            courseTutorDTO.setFirstName(course.getTutor().getUser().getFirstName());
            courseTutorDTO.setLastName(course.getTutor().getUser().getLastName());

            courseTutorDTOS.add(courseTutorDTO);
        }

        return courseTutorDTOS;
    }

    public GetFilterCategoriesResponse getFilterCategories() {
        List<CourseCategory> categoryList = courseCategoryService.getCourseCategories();
        List<String> courseCategoryList = new ArrayList<>();
        for (CourseCategory courseCategory : categoryList) {
            courseCategoryList.add(courseCategory.getCategory());
        }

        List<CourseLevel> levelList = courseLevelRespository.findAll();
        List<String> courseLevelList = new ArrayList<>();
        for (CourseLevel courseLevel : levelList) {
            courseLevelList.add(courseLevel.getCategory());
        }

        List<CoursePriceCategory> priceCategoryList = coursePriceCategoryRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        List<String> tutorList = new ArrayList<>();
        for (Course course : courses) {
            tutorList.add(getUserFullName(course.getTutor().getUser()));
        }
        return new GetFilterCategoriesResponse(
                courseCategoryList,
                tutorList.stream().distinct().collect(Collectors.toList()),
                courseLevelList,
                priceCategoryList
        );
    }

    public Map<String, Object> searchByValue(int page, String value, Long userId) throws EntityNotFoundException {
        Page<Course> coursePage = courseRepository.findAllByNameContaining(
                value,
                PageRequest.of(page, 10)
        );
        return getCoursesResponse(coursePage, userId);
    }

    public Map<String, Object> filterCourses(FilterCoursesRequest request) throws EntityNotFoundException {
        List<CourseCategory> categoryList = courseCategoryService.getCourseCategoryByName(request.getCategoryList());
        List<Tutor> tutorList = tutorProfileService.getTutorsByName(request.getTutorList());
        List<CourseType> courseTypeList = courseTypeRepository.findAllByTitleIn(request.getTypeList());
        Page<Course> coursePage = courseRepository.findAllByCourseCategoryInAndTutorInAndCourseTypeInAndCoursePriceCategoryIn(
                categoryList,
                tutorList,
                courseTypeList,
                request.getPriceList(),
                PageRequest.of(request.getPage(), 10)
        );
        return getCoursesResponse(coursePage, request.getUserId());
    }

    public Map<String, Object> getCoursesResponse(Page<Course> coursePage, Long userId) throws EntityNotFoundException {
        List<Course> courseList = coursePage.getContent();

        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courseList) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTO.setCategoryId(course.getCourseCategory().getId());
            courseDTO.setCategoryName(course.getCourseCategory().getCategory());
            courseDTO.setTutorId(course.getTutor().getId());
            courseDTO.setTutorName(course.getTutor().getUser().getFirstName() + " " + course.getTutor().getUser().getLastName());
            courseDTO.setImageUrl(getCourseImage(course.getImageUrl()));
            courseDTO.setEnrolledByCurrentUser(isCurrentUserEnrolled(userId, course));
            courseDTO.setSchedules(course.getSchedules());
            courseDTOS.add(courseDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", courseDTOS);
        response.put("current", coursePage.getNumber());
        response.put("total", coursePage.getTotalPages());
        return response;
    }

    public GetCourseByIdResponse getCourseById(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            return new GetCourseByIdResponse(
                    course.getId(),
                    course.getName(),
                    course.getDescription(),
                    course.getDuration(),
                    getUserFullName(course.getTutor().getUser()),
                    getCourseImage(course.getImageUrl()),
                    course.getRating(),
                    course.getPrice(),
                    course.getTutor().getId()
            );
        } else {
            return null;
        }
    }

    public GetCourseByTutorResponse getCourseByTutor(Long tutorId) throws EntityNotFoundException {
        Tutor tutor = tutorProfileService.getTutor(tutorId);
        Course course = courseRepository.findByTutor(tutor);
        return getCourseByTutorResponse(course);
    }

    private GetCourseByTutorResponse getCourseByTutorResponse(Course course) {
        return new GetCourseByTutorResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                getCourseImage(course.getImageUrl()),
                course.getPrice(),
                course.getCourseCategory(),
                course.getCourseType(),
                course.getSchedules(),
                course.getCourseDuration()
        );
    }

    private boolean isCurrentUserEnrolled(Long userId, Course course) throws EntityNotFoundException {
        if (!userRepository.findById(userId).isPresent()) {
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userId).get();

        boolean isEnrolled = false;
        Enrollment enrollment = enrollmentRepository.findByCourseAndUser(course, user);
        if (enrollment != null) {
            isEnrolled = true;
        }
        return isEnrolled;
    }

    public String getUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @SneakyThrows
    private byte[] getCourseImage(String url) {
        if (url != null) {
            String[] filename = url.trim().split("http://localhost:8080/api/courses/uploads/Courses/");
            return getImageByte(filename[1]);
        }
        return null;
    }

    public byte[] getImageByte(String filename) throws IOException {
        return fileStorageService.convert(filename);
    }

    private List<Schedule> saveAndGetSchedules(List<Schedule> request) {
        List<Schedule> schedules = new ArrayList<>();
        int i;
        for (i = 0; i < request.size(); i++) {
            Schedule newSchedule = request.get(i);
            Schedule schedule = Schedule
                    .builder()
                    .day(newSchedule.getDay())
                    .startTime(newSchedule.getStartTime())
                    .endTime(newSchedule.getEndTime())
                    .build();
            Schedule persisted = scheduleRepository.save(schedule);
            schedules.add(persisted);
        }
        return schedules;
    }

    public CourseCategory getCourseCategoryByName(String name) {
        return courseCategoryService.getCategoryByName(name);
    }

    public CourseType getCourseTypeByName(String name) {
        return courseTypeRepository.findByTitle(name);
    }

    public CoursePriceCategory getCoursePriceCategory(BigDecimal price) {
        List<CoursePriceCategory> coursePriceCategories = coursePriceCategoryRepository.findAll();
        CoursePriceCategory coursePriceCategory = null;
        if (!coursePriceCategories.isEmpty()) {
            for (CoursePriceCategory category : coursePriceCategories) {
                int minComparison = price.compareTo(category.getPriceMin());
                int maxComparison = price.compareTo(category.getPriceMax());
                if (minComparison >= 0 && maxComparison < 0) {
                    coursePriceCategory = category;
                }
            }
        }
        return coursePriceCategory;
    }

}
