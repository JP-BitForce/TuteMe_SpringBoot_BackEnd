package com.bitforce.tuteme.service;

import com.bitforce.tuteme.PageableEntity.PageableCoreQuestions;
import com.bitforce.tuteme.PageableEntity.PageableCoreTags;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewQuestionRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetAnswersResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetQuestionsPageResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.*;
import com.bitforce.tuteme.repository.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final VoteRepository voteRepository;
    private final AnswerRepository answerRepository;

    public OneStepService(TagRepository tagRepository,
                          UserRepository userRepository,
                          QuestionRepository questionRepository,
                          StudentProfileService studentProfileService,
                          TutorProfileService tutorProfileService,
                          VoteRepository voteRepository,
                          AnswerRepository answerRepository
    ) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.studentProfileService = studentProfileService;
        this.tutorProfileService = tutorProfileService;
        this.voteRepository = voteRepository;
        this.answerRepository = answerRepository;
    }

    public PageableCoreTags getAllTags(int page) {
        Page<Tag> tagList = tagRepository.findAll(PageRequest.of(page, 10));
        log.info("No of tag page found: {}", tagList.getTotalPages());
        return new PageableCoreTags(
                tagList.get().collect(Collectors.toList()),
                tagList.getTotalPages(),
                tagList.getNumber()
        );
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
                .answers(new ArrayList<>())
                .answered(false)
                .build();

        questionRepository.save(question);

        for (Tag tag : tagList) {
            tag.setNoOfQuestions(tag.getNoOfQuestions() + 1);
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
                        question.getAnswers().size()
                )).collect(Collectors.toList()),
                pageableQuestions.getTotal(),
                pageableQuestions.getCurrent()
        );
    }

    public List<Tag> searchTagByTitle(String title) {
        return tagRepository.findAllByTitleIsContaining(title);
    }

    public List<Tag> filterTagsByAlphabeticOrder() {
        return tagRepository.findByOrderByTitleAsc();
    }

    public GetQuestionsPageResponse filterQuestions(String type, int page) {
        Page<Question> questionPage;
        switch (type) {
            case "Unanswered":
                questionPage = filterUnansweredQuestions(page);
                break;
            case "Votes":
                questionPage = filterQuestionByVoteOrder(page);
                break;
            case "Newest":
                questionPage = filterQuestionByTime(page);
                break;
            default:
                return null;
        }
        PageableCoreQuestions pageableQuestions = getPageableCoreQuestions(questionPage);
        return getPageResponse(pageableQuestions);
    }

    private Page<Question> filterUnansweredQuestions(int page) {
        return questionRepository.findAllByAnsweredEquals(
                false,
                PageRequest.of(page, 10)
        );
    }

    private Page<Question> filterQuestionByVoteOrder(int page) {
        return questionRepository.findByOrderByVotesDesc(PageRequest.of(page, 10));
    }

    private Page<Question> filterQuestionByTime(int page) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().minusDays(3);
        return questionRepository.findAllByCreatedAtLessThanEqualAndCreatedAtGreaterThanOrderByCreatedAtDesc(
                start,
                end,
                PageRequest.of(page, 10)
        );
    }


    public String addVote(String uId, String qId) throws EntityNotFoundException {
        User user = getUser(Long.parseLong(uId));

        Long questionId = Long.parseLong(qId);
        Question question = getQuestion(questionId);
        question.setVotes(question.getVotes() + 1);

        Vote vote = Vote.builder().user(user).build();
        voteRepository.save(vote);
        List<Vote> votes = question.getVoteList();
        votes.add(vote);
        question.setVoteList(votes);
        questionRepository.save(question);
        return "vote added successfully";
    }

    public GetAnswersResponse getAnswers(Long id, Long userId) throws EntityNotFoundException {
        Question question = getQuestion(id);
        List<Vote> voteList = question.getVoteList();
        boolean currentUserVotedForQuestion = false;
        for (Vote vote : voteList) {
            if (vote.getUser().getId().equals(userId)) {
                currentUserVotedForQuestion = true;
                break;
            }
        }
        return new GetAnswersResponse(
                question.getAnswers().stream().map(answer -> new GetAnswersResponse.Answer(
                                answer.getId(),
                                answer.getContent(),
                                answer.getCreatedAt(),
                                answer.getVotes(),
                                getAuthorName(answer.getUser()),
                                getAuthorImageByte(answer.getUser().getId())
                        )
                ).collect(Collectors.toList()),
                currentUserVotedForQuestion
        );
    }

    public String postNewAnswer(Long uId, Long qId, String content) throws EntityNotFoundException {
        User user = getUser(uId);
        Question question = getQuestion(qId);
        Answer answer = Answer
                .builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .votes(0)
                .user(user)
                .build();
        answerRepository.save(answer);
        List<Answer> answers = question.getAnswers();
        answers.add(answer);
        question.setAnswers(answers);
        questionRepository.save(question);
        return "answer added successfully";
    }

    public GetQuestionsPageResponse filterByTag(String tag, int page) {
        List<Tag> tagList = tagRepository.findAllByTitleIsContaining(tag);
        Page<Question> questionPage = questionRepository.findAllByTagsIn(
                tagList,
                PageRequest.of(page, 10)
        );
        PageableCoreQuestions pageableQuestions = getPageableCoreQuestions(questionPage);
        return getPageResponse(pageableQuestions);
    }

    public GetQuestionsPageResponse searchQuestionByValue(String value, int page) {
        Page<Question> questionPage = questionRepository.findAllByTitleIsContaining(
                value,
                PageRequest.of(page, 10)
        );
        PageableCoreQuestions pageableQuestions = getPageableCoreQuestions(questionPage);
        return getPageResponse(pageableQuestions);
    }


    private User getUser(Long userId) throws EntityNotFoundException {
        if (!userRepository.findById(userId).isPresent()) {
            log.error("user not found for id: {}", userId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        return userRepository.findById(userId).get();
    }

    private Question getQuestion(Long qId) throws EntityNotFoundException {
        if (!questionRepository.findById(qId).isPresent()) {
            log.error("question entity not found for given id: {}", qId);
            throw new EntityNotFoundException("QUESTION_NOT_FOUND");
        }
        return questionRepository.findById(qId).get();
    }

    private PageableCoreQuestions getPageableCoreQuestions(Page<Question> questionPage) {
        return new PageableCoreQuestions(
                questionPage
                        .get()
                        .collect(Collectors.toList()),
                questionPage.getTotalPages(),
                questionPage.getNumber()
        );
    }

    private GetQuestionsPageResponse getPageResponse(PageableCoreQuestions pageableQuestions) {
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
                        question.getAnswers().size()
                )).collect(Collectors.toList()),
                pageableQuestions.getTotal(),
                pageableQuestions.getCurrent()
        );
    }

    private String getAuthorName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }

    @SneakyThrows
    private byte[] getAuthorImageByte(Long authorId) {
        if (!userRepository.findById(authorId).isPresent()) {
            log.error("user not found for id: {}", authorId);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(authorId).get();
        if (user.getImageUrl() != null) {
            String[] filename;
            if (user.getType().equals("student")) {
                filename = user.getImageUrl().trim().split("http://localhost:8080/api/student/profile/uploads/profilePicture/student/");
                return studentProfileService.getImageByte(filename[1]);
            } else {
                filename = user.getImageUrl().trim().split("http://localhost:8080/api/tutor/profile/uploads/profilePicture/tutor/");
                return tutorProfileService.getImageByte(filename[1]);
            }
        } else {
            return null;
        }
    }
}
