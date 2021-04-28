package com.bitforce.tuteme.dto.ServiceRequest;

import com.bitforce.tuteme.model.CoursePriceCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCoursesRequest {
    private List<String> categoryList;
    private List<String> tutorList;
    private List<String> typeList;
    private List<CoursePriceCategory> priceList;
    private int page;
    private Long userId;
}
