package com.bitforce.tuteme.service;

import com.bitforce.tuteme.model.Skill;
import com.bitforce.tuteme.repository.SkillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill createSkill(Skill newSkill) {
        Skill skill = new Skill();
        skill.setName(newSkill.getName());
        skill.setCategory(newSkill.getCategory());
        return skillRepository.save(skill);
    }

}
