package com.bitforce.tuteme.dto.ServiceResponse;

import com.bitforce.tuteme.model.CoursePriceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetFilterCategoriesResponse {
    List<String> courseCategoryList = new ArrayList<>();
    List<String> tutorList = new ArrayList<>();
    List<String> courseLevelList = new ArrayList<>();
    List<CoursePriceCategory> coursePriceCategoryList = new ArrayList<>();
}
