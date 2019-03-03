package com.example.studyrestapi.demo.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class EventResource extends Resource<Event> {
   // ResourceSupport를 상속받으면 @JsonUnwrapped를 Event에 추가하면됨. 그러나 Resource를 상속받는게 더 간단하고, 이미 JsonUpwrapped제공
    // json데이터로 변환시, 객체는 제외하고 데이터만 json으로변환되는 어노테이션

    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
