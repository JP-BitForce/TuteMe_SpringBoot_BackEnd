package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.AddNewEventRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetEventsResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.model.Event;
import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.EventRepository;
import com.bitforce.tuteme.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public GetEventsResponse addEvent(AddNewEventRequest addNewEventRequest) throws EntityNotFoundException {
        Long userid = Long.parseLong(addNewEventRequest.getUserId());
        if (!userRepository.findById(userid).isPresent()) {
            log.error("user not found for id: {}", userid);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userid).get();
        Event event = Event
                .builder()
                .title(addNewEventRequest.getTitle())
                .description(addNewEventRequest.getDescription())
                .backgroundColor(addNewEventRequest.getBackgroundColor())
                .start(parseDate(addNewEventRequest.getStart()))
                .end(parseDate(addNewEventRequest.getEnd()))
                .user(user)
                .build();
        List<Event> events = eventRepository.findAllByUser(user);
        events.add(event);
        eventRepository.save(event);
        return new GetEventsResponse(
                events.stream().map(eve -> new GetEventsResponse.Event(
                                eve.getId(),
                                eve.getTitle(),
                                eve.getStart(),
                                eve.getEnd(),
                                eve.getBackgroundColor(),
                                eve.getDescription()
                        )
                ).collect(Collectors.toList())
        );
    }


    public GetEventsResponse getEvents(String userId) throws EntityNotFoundException {
        Long userid = Long.parseLong(userId);
        if (!userRepository.findById(userid).isPresent()) {
            log.error("user not found for id: {}", userid);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userid).get();
        List<Event> events = eventRepository.findAllByUser(user);
        return new GetEventsResponse(
                events.stream().map(event -> new GetEventsResponse.Event(
                                event.getId(),
                                event.getTitle(),
                                event.getStart(),
                                event.getEnd(),
                                event.getBackgroundColor(),
                                event.getDescription()
                        )
                ).collect(Collectors.toList())
        );
    }

    private LocalDateTime parseDate(String strDate) {
        ZoneId utc = ZoneId.of("UTC");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(strDate);
        return LocalDateTime.from(zonedDateTime.toInstant().atZone(utc));
    }

}
