package com.example.studyrestapi.demo.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() !=0){
            //global error 여러가지 값의 조합에 걸렸을 때
            errors.reject("wrongPrices","Prives are wrong ");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            //field error 각각의 필드에 대한 에러
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        //TODO BeginEventDateTIme
        //TODO CloseEnrollmentDateTime
    }
}
