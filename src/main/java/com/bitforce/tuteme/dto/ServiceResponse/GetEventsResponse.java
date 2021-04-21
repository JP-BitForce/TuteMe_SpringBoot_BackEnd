package com.bitforce.tuteme.dto.ServiceResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsResponse {
    List<Event> events = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event {
        private Long id;
        private String title;
        private LocalDateTime start;
        private LocalDateTime end;
        private String backgroundColor;
        private String description;
    }

}
