package com.example.studyrestapi.demo.common;

import com.example.studyrestapi.demo.index.IndexController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ErrorResource extends Resource<Errors> {
    public ErrorResource(Errors content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        //error 발생할 때 index링크도 함께 전달
        //배열이 전달되어서 @jsonUnwrappeed 적용 안됨.
    }
}
