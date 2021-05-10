package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.UserSkillsDTO;
import com.bitforce.tuteme.model.Skill;
import com.bitforce.tuteme.model.UserSkills;
import com.bitforce.tuteme.repository.SkillRepository;
import com.bitforce.tuteme.repository.UserRepository;
import com.bitforce.tuteme.repository.UserSkillsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserSkillService {

    private final UserSkillsRepository userSkillsRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;


    public Map<String,Set<Skill>> getAllUserSkillsForUser(Long userId) {
        UserSkills userSkills = userSkillsRepository.findByUserId(userId);

        Set<Skill> subjectSkills =new HashSet<>();
        Set<Skill> topSkills =new HashSet<>();
        Set<Skill> techSkills =new HashSet<>();

        if(userSkills.getSubjectSkills() != null){
            subjectSkills= userSkills.getSubjectSkills();
        }
        if (userSkills.getTopSkills() != null){
             topSkills = userSkills.getTopSkills();
        }
        if(userSkills.getTechSkills()!=null){
            techSkills = userSkills.getTechSkills();
        }

        Map<String,Set<Skill>> skills = new HashMap();
        skills.put("subjectSkills",subjectSkills);
        skills.put("topSkills",topSkills);
        skills.put("techSkills",techSkills);
        return skills;
    }

    public UserSkills createUserSkills(UserSkillsDTO newSkills) {
        Set<Long> subjectSkillIds = newSkills.getSubjectSkills();
        Set<Long> topSkillIds = newSkills.getTopSkills();
        Set<Long> techSkillIds = newSkills.getTechSkills();
        Set<Skill> subjectSkills = new HashSet<>();
        Set<Skill> topSkills = new HashSet<>();
        Set<Skill> techSkills = new HashSet<>();
        UserSkills userSkills;

        if(userSkillsRepository.existsByUserId(newSkills.getUserId())){
            userSkills= userSkillsRepository.findByUserId(newSkills.getUserId());
            subjectSkills.addAll(userSkills.getSubjectSkills());
            topSkills.addAll(userSkills.getTopSkills());
            techSkills.addAll(userSkills.getTechSkills());

            if (subjectSkillIds != null) {
                subjectSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    subjectSkills.add(skill);
                });
                userSkills.setSubjectSkills(subjectSkills);
            }
            if (topSkillIds != null) {
                topSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    topSkills.add(skill);
                });
                userSkills.setTopSkills(topSkills);
            }
            if (techSkillIds != null) {
                techSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    techSkills.add(skill);
                });
                userSkills.setTechSkills(techSkills);
            }
        }else {
            userSkills= new UserSkills();
            userSkills.setUser(userRepository.findById(newSkills.getUserId()).get());
            if (subjectSkillIds != null) {
                subjectSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    subjectSkills.add(skill);
                });
                userSkills.setSubjectSkills(subjectSkills);
            }
            if (topSkillIds != null) {
                topSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    topSkills.add(skill);
                });
                userSkills.setTopSkills(topSkills);
            }
            if (techSkillIds != null) {
                techSkillIds.forEach(skill_id -> {
                    Skill skill = skillRepository.findById(skill_id).
                            orElseThrow(() -> new RuntimeException("Error: Skill is not found."));
                    techSkills.add(skill);
                });
                userSkills.setTechSkills(techSkills);
            }
        }
        return userSkillsRepository.save(userSkills);
    }
}
