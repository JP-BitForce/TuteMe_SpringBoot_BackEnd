package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ControllerResponse.UserSkillsResponse;
import com.bitforce.tuteme.dto.UserSkillsDTO;
import com.bitforce.tuteme.model.UserSkills;
import com.bitforce.tuteme.service.UserSkillService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/user-skills")
public class UserSkillsController {

    private final UserSkillService userSkillService;

    @GetMapping()
    public UserSkillsResponse getAllSkillsForUser(@RequestParam Long userId){
        return userSkillService.getAllUserSkillsForUser(userId);
    }

    @PutMapping
    public UserSkills createSkill(@RequestBody UserSkillsDTO newSkills){
        return userSkillService.createUserSkills(newSkills);
    }

}
