package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.PageableEntity.PageableCoreTags;
import com.bitforce.tuteme.dto.ApiResponse;
import com.bitforce.tuteme.dto.ControllerRequest.AddNewQuestionControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.AddQuestionVoteControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.PostAnswerControllerRequest;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewQuestionRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetAnswersResponse;
import com.bitforce.tuteme.dto.ServiceResponse.GetQuestionsPageResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Tag;
import com.bitforce.tuteme.service.OneStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/oneStep")
public class OneStepController {
    private static final Logger log = LoggerFactory.getLogger(OneStepController.class);

    private final OneStepService oneStepService;

    @Autowired
    public OneStepController(OneStepService oneStepService) {
        this.oneStepService = oneStepService;
    }

    @GetMapping(value = "/getTags/{page}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllTags(@PathVariable int page) {
        try {
            PageableCoreTags pageableCoreTags = oneStepService.getAllTags(page);
            return new ResponseEntity<>(pageableCoreTags, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get all tags");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/add_new_question")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> postNewQuestion(@RequestBody AddNewQuestionControllerRequest request) {
        try {
            AddNewQuestionRequest addNewQuestionRequest = new AddNewQuestionRequest(
                    request.getUserId(),
                    request.getTitle(),
                    request.getContent(),
                    request.getTags()
            );
            String response = oneStepService.postNewQuestion(addNewQuestionRequest);
            ApiResponse apiResponse = new ApiResponse(true, response);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to post new question due to bad request for given userId: {}", request.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to post new question");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/getQuestions/{page}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getQuestions(@PathVariable int page) {
        try {
            GetQuestionsPageResponse response = oneStepService.getQuestions(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get all question");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/searchTagByTitle/{title}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchTagByTitle(@PathVariable String title) {
        try {
            List<Tag> list = oneStepService.searchTagByTitle(title);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get all question");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/filterTagsByAlphabet")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterTagsByAlphabet() {
        try {
            List<Tag> list = oneStepService.filterTagsByAlphabeticOrder();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get all question");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/filterQuestionsByType")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterQuestionsByType(@RequestParam String type, @RequestParam int page) {
        try {
            GetQuestionsPageResponse response = oneStepService.filterQuestions(type, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to get all question");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/add_question_vote")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> addQuestionVote(@RequestBody AddQuestionVoteControllerRequest request) {
        try {
            String res = oneStepService.addVote(
                    request.getUserId(),
                    request.getQuestionId()
            );
            ApiResponse apiResponse = new ApiResponse(true, res);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to add vote for question:=> QID: {}, UId: {}", request.getQuestionId(), request.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to add vote for question with id of: {}", request.getQuestionId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/get_question_answers")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getQuestionAnswers(@RequestParam String uId, @RequestParam String qId) {
        try {
            GetAnswersResponse getAnswersResponse = oneStepService.getAnswers(
                    Long.parseLong(qId),
                    Long.parseLong(uId)
            );
            return new ResponseEntity<>(getAnswersResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get answers for question id: {}", qId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get answers");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/post_answer")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> postAnswer(@RequestBody PostAnswerControllerRequest request) {
        try {
            String res = oneStepService.postNewAnswer(
                    Long.parseLong(request.getUserId()),
                    Long.parseLong(request.getQuestionId()),
                    request.getAnswer()
            );
            ApiResponse apiResponse = new ApiResponse(true, res);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to add answer for question:=> QID: {}, UId: {}", request.getQuestionId(), request.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to add answer for question with id of: {}", request.getQuestionId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/filter_question_by_tag")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> filterQuestionByTag(@RequestParam String tag, @RequestParam int page) {
        try {
            GetQuestionsPageResponse response = oneStepService.filterByTag(tag, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to filter questions");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/search_question")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchQuestion(@RequestParam String value, @RequestParam int page) {
        try {
            GetQuestionsPageResponse response = oneStepService.searchQuestionByValue(value, page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Unable to filter questions");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
