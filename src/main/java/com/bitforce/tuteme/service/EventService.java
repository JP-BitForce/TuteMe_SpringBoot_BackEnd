package com.bitforce.tuteme.service;

import com.bitforce.tuteme.dto.ServiceRequest.AddNewEventRequest;
import com.bitforce.tuteme.dto.ServiceRequest.UpdateEventRequest;
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
                .start(parse(addNewEventRequest.getStart()))
                .end(parse(addNewEventRequest.getEnd()))
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

    public GetEventsResponse updateEvent(UpdateEventRequest request) throws EntityNotFoundException {
        Long userid = Long.parseLong(request.getUserId());
        if (!userRepository.findById(userid).isPresent()) {
            log.error("user not found for id: {}", userid);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userid).get();

        Long eventId = Long.parseLong(request.getEventId());
        if (!eventRepository.findById(eventId).isPresent()) {
            log.error("Event not found for id: {}", eventId);
            throw new EntityNotFoundException("EVENT_NOT_FOUND");
        }
        Event event = eventRepository.findById(eventId).get();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStart(parse(request.getStart()));
        event.setEnd(parse(request.getEnd()));
        event.setBackgroundColor(request.getBackgroundColor());
        eventRepository.save(event);

        List<Event> events = eventRepository.findAllByUser(user);
        return new GetEventsResponse(
                events.stream().map(evt -> new GetEventsResponse.Event(
                                evt.getId(),
                                evt.getTitle(),
                                evt.getStart(),
                                evt.getEnd(),
                                evt.getBackgroundColor(),
                                evt.getDescription()
                        )
                ).collect(Collectors.toList())
        );
    }

    public GetEventsResponse deleteEvent(String userId, String eventId) throws EntityNotFoundException {
        Long userid = Long.parseLong(userId);
        if (!userRepository.findById(userid).isPresent()) {
            log.error("user not found for id: {}", userid);
            throw new EntityNotFoundException("USER_NOT_FOUND");
        }
        User user = userRepository.findById(userid).get();

        Long eventid = Long.parseLong(eventId);
        if (!eventRepository.findById(eventid).isPresent()) {
            log.error("Event not found for id: {}", eventId);
            throw new EntityNotFoundException("EVENT_NOT_FOUND");
        }
        Event event = eventRepository.findById(eventid).get();
        eventRepository.delete(event);
        List<Event> events = eventRepository.findAllByUser(user);
        return new GetEventsResponse(
                events.stream().map(evt -> new GetEventsResponse.Event(
                                evt.getId(),
                                evt.getTitle(),
                                evt.getStart(),
                                evt.getEnd(),
                                evt.getBackgroundColor(),
                                evt.getDescription()
                        )
                ).collect(Collectors.toList())
        );
    }

    private LocalDateTime parse(String date) {
        return LocalDateTime.parse(date);
    }
}
