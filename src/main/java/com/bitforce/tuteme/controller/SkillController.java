package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.Skill;
import com.bitforce.tuteme.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    @GetMapping()
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @PostMapping
    public Skill createSkill(@RequestBody Skill newSkill) {
        return skillService.createSkill(newSkill);
    }

}
