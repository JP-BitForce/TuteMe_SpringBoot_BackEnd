package com.bitforce.tuteme.dto.ControllerResponse;

import com.bitforce.tuteme.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsResponse {
    private Long id;
    private  Long userId;
    public Set<Skill> skills;
}
