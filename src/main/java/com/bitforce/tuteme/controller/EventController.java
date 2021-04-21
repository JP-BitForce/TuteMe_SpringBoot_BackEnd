package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.dto.ControllerRequest.AddNewEventControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.DeleteEventControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.GetEventsControllerRequest;
import com.bitforce.tuteme.dto.ControllerRequest.UpdateEventControllerRequest;
import com.bitforce.tuteme.dto.ServiceRequest.AddNewEventRequest;
import com.bitforce.tuteme.dto.ServiceRequest.UpdateEventRequest;
import com.bitforce.tuteme.dto.ServiceResponse.GetEventsResponse;
import com.bitforce.tuteme.exception.EntityNotFoundException;
import com.bitforce.tuteme.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/event")
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/addEvent")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> addNewEvent(@RequestBody AddNewEventControllerRequest request) {
        try {
            AddNewEventRequest addNewEventRequest = new AddNewEventRequest(
                    request.getUserId(),
                    request.getTitle(),
                    request.getDescription(),
                    request.getStart(),
                    request.getEnd(),
                    request.getBackgroundColor()
            );
            GetEventsResponse response = eventService.addEvent(addNewEventRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to add new event due to bad request for given id: {}", request.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to add new event");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/getEvents")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getEvents(@RequestBody GetEventsControllerRequest request) {
        try {
            GetEventsResponse response = eventService.getEvents(request.getUserId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to get events due to bad request for given userId: {}", request.getUserId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to get events");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/updateEvent")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateEvent(@RequestBody UpdateEventControllerRequest request) {
        try {
            UpdateEventRequest updateEventRequest = new UpdateEventRequest(
                    request.getUserId(),
                    request.getEventId(),
                    request.getTitle(),
                    request.getDescription(),
                    request.getStart(),
                    request.getEnd(),
                    request.getBackgroundColor()
            );
            GetEventsResponse response = eventService.updateEvent(updateEventRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to update event due to bad request for given userId: {}, eventId: {}",
                    request.getUserId(),
                    request.getEventId()
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to update event");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping(value = "/deleteEvent")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteEvent(@RequestBody DeleteEventControllerRequest request) {
        try {
            GetEventsResponse response = eventService.deleteEvent(request.getUserId(), request.getEventId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Unable to delete event due to bad request for given userId: {}, eventId: {}",
                    request.getUserId(),
                    request.getEventId()
            );
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Unable to delete event");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
