package com.example.studyrestapi.demo.events;

import com.example.studyrestapi.demo.accounts.Account;
import com.example.studyrestapi.demo.accounts.AccountRepository;
import com.example.studyrestapi.demo.accounts.AccountRole;
import com.example.studyrestapi.demo.accounts.AccountService;
import com.example.studyrestapi.demo.common.AppProperties;
import com.example.studyrestapi.demo.common.BaseControllerTest;
import com.example.studyrestapi.demo.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void setUp(){
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                    .name("spring")
                    .description("REST API")
                    .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                    .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                    .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                     .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                    .basePrice(100)
                    .maxPrice(200)
                    .limitOfEnrollment(100)
                    .location("강남역 D2")
                    .build();

        //when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON) //HAL 형식으로 받고 싶다.
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("create-event",
                    links(
                        linkWithRel("self").description("Link to self"),
                        linkWithRel("query-events").description("link to query events"),
                        linkWithRel("update-event").description("link to update an existing"),
                        linkWithRel("profile").description("link to document")
                    ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("dscription of new eventt"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("location").description("location of new event")
                                ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        //relaxed를 prefix로 사용하면, 필요한 것만 정의하여 문서를 만들 수 있다.
                        //여기서는 relaxed를 안하면 테스트가 깨짐. link정보에 대해서는 지금 response에서 검증을 하지 않아서..
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("dscription of new eventt"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of beginEnrollmentDateTime"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of closeEnrollmentDateTime"),
                                fieldWithPath("beginEventDateTime").description("date time of beginEventDateTime"),
                                fieldWithPath("endEventDateTime").description("date time of endEventDateTime"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.profile.href").description("profile"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update event")

                        )

                ))
        ;

    }


    private String getBearerToken() throws Exception {
        return "Bearer"+getAccessToken(true);
    }

    private String getBearerToken(boolean needToCreateAccount) throws Exception {
        return "Bearer"+getAccessToken(needToCreateAccount);
    }

    private String getAccessToken(boolean needToCreateAccount) throws Exception {
        //Given
        String username = appProperties.getAdminUsername();
        String password = appProperties.getAdminPassword();
        if (needToCreateAccount){
            createAccount(username,password);

        }

        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();


        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                                    .with(httpBasic(clientId, clientSecret)) //basicAuth header 생성
                                    .param("username", username)
                                    .param("password", password)
                                    .param("grant_type", "password"));

        String responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }

    private Account createAccount(String username, String password) {
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet()))
                .build();
        return this.accountService.saveAccount(account);
    }


    //입력 값 이외에 다른 필드 보낼 때 badRequest 테스트
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_BAD_REQUEST() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .free(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        //when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON) //HAL 형식으로 받고 싶다.
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //validaton test
    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();
        this.mockMvc.perform(post("/api/events")
                         .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest());
    }


    @Test
    @TestDescription("입력 값이 잘못된 경우에 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto event = EventDto.builder()
                .name("spring")
                .description("REST API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();

        //when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON) //HAL 형식으로 받고 싶다.
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists()) //index보라고.
        ;

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //Given
        IntStream.range(0,30).forEach(this::genereateEvent);

        //when
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("page").exists())
                    .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.profile").exists())
                    .andDo(document("query-events"))
        ;

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEventsWithAutentication() throws Exception{
        //Given
        IntStream.range(0,30).forEach(this::genereateEvent);

        //when
        this.mockMvc.perform(get("/api/events")
                .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                .param("page","1")
                .param("size","10")
                .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))
        ;

    }


    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws  Exception{
        //Given
        Account account = this.createAccount(appProperties.getAdminUsername(), appProperties.getAdminPassword());
        Event event = this.genereateEvent(100,account);

        //When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("name").exists())
                    .andExpect(jsonPath("id").exists())
                    .andExpect(jsonPath("_links.self").exists())
                    .andExpect(jsonPath("_links.profile").exists())
                    .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 대 404 응답받기")
    public void getEvent404() throws  Exception{
        //When & Then

        this.mockMvc.perform(get("/api/events/11883"))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception{
        //Given

        Account account = this.createAccount(appProperties.getAdminUsername(), appProperties.getAdminPassword());
        Event event = this.genereateEvent(200, account);

        EventDto eventDto =  this.modelMapper.map(event,EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                               .header(HttpHeaders.AUTHORIZATION,  getBearerToken(false))
                               .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.objectMapper.writeValueAsString(eventDto))
                             )
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("name").value(eventName))
                            .andExpect(jsonPath("_links.self").exists())
                        ;

    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception{
        //Given
        Event event = this.genereateEvent(200);

        EventDto eventDto =  new EventDto();

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                            .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content((this.objectMapper.writeValueAsString(eventDto)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception{
        //Given
        Event event = this.genereateEvent(200);

        EventDto eventDto =  this.modelMapper.map(event,EventDto.class);
        eventDto.setBasePrice(200000);
        eventDto.setMaxPrice(1000);
        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                    .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception{
        //Given
        Event event = this.genereateEvent(200);
        EventDto eventDto =  this.modelMapper.map(event,EventDto.class);

        //When & Then
        this.mockMvc.perform(put("/api/events/1231123")
                        .header(HttpHeaders.AUTHORIZATION,  getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .content((this.objectMapper.writeValueAsString(eventDto)))
                 )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;

    }

    private Event genereateEvent(int index, Account account){
        Event event = buildEvent(index);
        event.setManager(account);
        return this.eventRepository.save(event);
    }

    private Event genereateEvent(int index) {
        Event event = buildEvent(index);
        return this.eventRepository.save(event);
    }

    private Event buildEvent(int index) {
        return Event.builder()
                            .name("event"+index)
                            .description("test event")
                            .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                            .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                            .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                            .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                            .basePrice(100)
                            .maxPrice(200)
                            .limitOfEnrollment(100)
                            .location("강남역 D2")
                            .free(false)
                            .offline(true)
                            .eventStatus(EventStatus.DRAFT)
                            .build();
    }
}