package com.example.studyrestapi.demo.events;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
//produces 설정하면 응답값으로 저 미디어 타입을 보내겠다.
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors ){
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }
        Event event = modelMapper.map(eventDto,Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
