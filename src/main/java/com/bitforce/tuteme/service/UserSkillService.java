package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.UserSkillsDTO;
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


    public Map<String, Set<String>> getAllUserSkillsForUser(Long userId) {
        UserSkills userSkills = userSkillsRepository.findByUserId(userId);

        Set<String> subjectSkills = new HashSet<>();
        Set<String> topSkills = new HashSet<>();
        Set<String> techSkills = new HashSet<>();

        if (userSkills.getSubjectSkills() != null) {
            subjectSkills = userSkills.getSubjectSkills();
        }
        if (userSkills.getTopSkills() != null) {
            topSkills = userSkills.getTopSkills();
        }
        if (userSkills.getTechSkills() != null) {
            techSkills = userSkills.getTechSkills();
        }

        Map<String, Set<String>> skills = new HashMap();
        skills.put("subjectSkills", subjectSkills);
        skills.put("topSkills", topSkills);
        skills.put("techSkills", techSkills);
        return skills;
    }

    public UserSkills createUserSkills(UserSkillsDTO newSkills) {
        Set<String> newSubjectSkills = newSkills.getSubjectSkills();
        Set<String> newTopSkills = newSkills.getTopSkills();
        Set<String> newTechSkills = newSkills.getTechSkills();
        UserSkills userSkills;

        Set<String> subjectSkills = new HashSet<>();
        Set<String> topSkills = new HashSet<>();
        Set<String> techSkills = new HashSet<>();

        if (userSkillsRepository.existsByUserId(newSkills.getUserId())) {
            userSkills = userSkillsRepository.findByUserId(newSkills.getUserId());

        } else {
            userSkills = new UserSkills();
            userSkills.setUser(userRepository.findById(newSkills.getUserId()).get());
        }
        if (newSubjectSkills != null) {
            subjectSkills.addAll(newSubjectSkills);
            userSkills.setSubjectSkills(subjectSkills);
        }
        if (newTechSkills != null) {
            topSkills.addAll(newTopSkills);
            userSkills.setTopSkills(topSkills);
        }
        if (newTechSkills != null) {
            techSkills.addAll(newTechSkills);
            userSkills.setTechSkills(techSkills);
        }
        return userSkillsRepository.save(userSkills);
    }
}
