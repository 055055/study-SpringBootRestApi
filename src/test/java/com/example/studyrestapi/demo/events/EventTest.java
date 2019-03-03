package com.example.studyrestapi.demo.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    //junitparams 사용으로 테스트 시, 파라미터 사용 가능
    @Parameters(method = "paramsForTestFre")
/*
    @Parameters({
            "0, 0, true",
            "100, 0, false",
            "0, 100, false"
    })*/
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event =Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }
    private Object[] paramsForTestFre(){
        return new Object[]{
                new Object[] {0,0,true},
                new Object[] {100,0,false},
                new Object[] {0,100,false},
                new Object[] {100,200,false},
        };
    }

    @Test
    @Parameters(method = "paramsForTestOffLine")
    public void testOffLine(String location, boolean isOffLine){
        //given
       Event event =Event.builder()
                .location(location)
                .build();
        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffLine);


    }

    private Object[] paramsForTestOffLine(){
        return new Object[]{
                new Object[] {"강남",true},
                new Object[] {null,false},
                new Object[] {" ",false},
        };
    }
}