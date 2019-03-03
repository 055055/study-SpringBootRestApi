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

    @Test
    public void testFree(){
        //given
        Event event =Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isTrue();


        //given
        event =Event.builder()
                .basePrice(10)
                .maxPrice(0)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();



        //given
        event =Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffLine(){
        //given
       Event event =Event.builder()
                .location("강남역 D2")
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

        //given
         event =Event.builder()
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isFalse();

    }
}