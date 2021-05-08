package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ControllerResponse.UserSkillsResponse;
import com.bitforce.tuteme.dto.UserSkillsDTO;
import com.bitforce.tuteme.model.Skill;
import com.bitforce.tuteme.model.UserSkills;
import com.bitforce.tuteme.repository.SkillRepository;
import com.bitforce.tuteme.repository.UserRepository;
import com.bitforce.tuteme.repository.UserSkillsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserSkillService {

    private final UserSkillsRepository userSkillsRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;


    public UserSkillsResponse getAllUserSkillsForUser(Long userId) {
        UserSkills userSkills = userSkillsRepository.findByUserId(userId);
        UserSkillsResponse userSkillsResponse = new UserSkillsResponse();

        userSkillsResponse.setId(userSkills.getId());
        userSkillsResponse.setUserId(userSkills.getUser().getId());
        userSkillsResponse.setSkills(userSkills.getSkills());
        return userSkillsResponse;
    }

    public UserSkills createUserSkills(UserSkillsDTO newSkills) {
        Set<Long> skillIds = newSkills.getSkills();
        Set<Skill> skills = new HashSet<>();

        UserSkills userSkills;
        if(userSkillsRepository.existsByUserId(newSkills.getUserId())){
            userSkills= userSkillsRepository.findByUserId(newSkills.getUserId());
            skills.addAll(userSkills.getSkills());
            if (skillIds != null) {
                skillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Department is not found."));
                    skills.add(skill);
                });
                userSkills.setSkills(skills);
            }
        }else {
            userSkills= new UserSkills();
            userSkills.setUser(userRepository.findById(newSkills.getUserId()).get());
            if (skillIds != null) {
                skillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Department is not found."));
                    skills.add(skill);
                });
                userSkills.setSkills(skills);
            }
        }
        return userSkillsRepository.save(userSkills);
    }
}
