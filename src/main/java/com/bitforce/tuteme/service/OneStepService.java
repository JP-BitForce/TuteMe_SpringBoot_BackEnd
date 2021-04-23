package com.bitforce.tuteme.service;

import com.bitforce.tuteme.PageableEntity.PageableCoreQuestions;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewQuestionRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetQuestionsPageResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Question;
import com.bitforce.tuteme.model.Tag;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.QuestionRepository;
import com.bitforce.tuteme.repository.TagRepository;
import com.bitforce.tuteme.repository.UserRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OneStepService {
    private final Logger log = LoggerFactory.getLogger(OneStepService.class);

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final StudentProfileService studentProfileService;
    private final TutorProfileService tutorProfileService;

    public OneStepService(TagRepository tagRepository,
                          UserRepository userRepository,
                          QuestionRepository questionRepository,
                          StudentProfileService studentProfileService,
                          TutorProfileService tutorProfileService
    ) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.studentProfileService = studentProfileService;
        this.tutorProfileService = tutorProfileService;
    }

    public List<Tag> getAllTags() {
        List<Tag> tagList = tagRepository.findAll();
        log.info("No of tags found: {}", tagList.size());
        return tagList;
    }

    public String postNewQuestion(AddNewQuestionRequest request) throws EntityNotFoundException {
        Long userId = Long.parseLong(request.getUserId());
        if (!userRepository.findById(userId).isPresent()) {
            log.error("user not found for id: {}", userId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userId).get();
        List<Tag> tagList = tagRepository.findAllByTitleIn(request.getTags());
        Question question = Question
                .builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .tags(tagList)
                .user(user)
                .votes(0)
                .build();

        questionRepository.save(question);

        for(Tag tag: tagList) {
            tag.setNoOfQuestions(tag.getNoOfQuestions()+1);
            tagRepository.save(tag);
        }

        log.info("New question added :=> id : {}, title: {}, createdAt: {}",
                question.getId(),
                question.getTitle(),
                question.getCreatedAt()
        );
        return "question added successfully";
    }

    public GetQuestionsPageResponse getQuestions(int page) {
        Page<Question> questionPage = questionRepository.findAll(PageRequest.of(page, 10));
        PageableCoreQuestions pageableQuestions = new PageableCoreQuestions(
                questionPage
                        .get()
                        .collect(Collectors.toList()),
                questionPage.getTotalPages(),
                questionPage.getNumber()
        );
        return new GetQuestionsPageResponse(
                pageableQuestions.getQuestions().stream().map(question -> new GetQuestionsPageResponse.Question(
                        question.getId(),
                        question.getTitle(),
                        question.getContent(),
                        question.getCreatedAt(),
                        question.getTags(),
                        getAuthorName(question.getUser()),
                        getAuthorImageByte(question.getUser().getId()),
                        question.getVotes(),
                        0
                )).collect(Collectors.toList()),
                pageableQuestions.getTotal(),
                pageableQuestions.getCurrent()
        );
    }

    public List<Tag> searchTagByTitle(String title) {
       return tagRepository.findAllByTitleIsContaining(title);
    }

    public List<Tag> filterTagsByAlphabeticOrder() {
        return  tagRepository.findByOrderByTitleAsc();
    }

    public String getAuthorName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @SneakyThrows
    public byte[] getAuthorImageByte(Long authorId) {
        if (!userRepository.findById(authorId).isPresent()) {
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
}
