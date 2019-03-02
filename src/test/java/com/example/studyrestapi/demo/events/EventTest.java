package com.example.studyrestapi.demo.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                    .name("learn REST API")
                    .description("REST API decelopment with Spring")
                    .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //given
        String name = "Event";
        String descriptiong = "Spring";

        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(descriptiong);

        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(descriptiong);

    }
}