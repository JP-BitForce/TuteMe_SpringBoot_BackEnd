package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.UserSkillsDTO;
import com.bitforce.tuteme.model.UserSkills;
import com.bitforce.tuteme.service.UserSkillService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/user-skills")
public class UserSkillsController {

    private final UserSkillService userSkillService;

    @GetMapping("/get_skills")
    public Map<String, Set<String>> getAllSkillsForUser(@RequestParam Long userId) {
        return userSkillService.getAllUserSkillsForUser(userId);
    }

    @PutMapping("/update_skills")
    public UserSkills createSkill(@RequestBody UserSkillsDTO newSkills) {
        return userSkillService.createUserSkills(newSkills);
    }

}
