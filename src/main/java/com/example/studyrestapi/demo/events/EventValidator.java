package com.example.studyrestapi.demo.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors erros){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() !=0){
            erros.rejectValue("basePrice","wrongValue","basePrice Is Wrong");
            erros.rejectValue("maxPrice","wrongValue","maxPrice Is Wrong");
        }

       LocalDateTime endEventDateTime =  eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
            endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
        ){
            erros.rejectValue("endEventDateTime","wrongValue","endEventDateTime is wrong");
        }

        //TODO BeginEventDateTIme
        //TODO CloseEnrollmentDateTime
    }
}
