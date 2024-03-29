package com.bitforce.tuteme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsDTO {
    private Long userId;
    private Set<String> subjectSkills;
    private Set<String> topSkills;
    private Set<String> techSkills;
}
