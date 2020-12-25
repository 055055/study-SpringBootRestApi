package com.example.studyrestapi.demo.configs;

import com.example.studyrestapi.demo.accounts.Account;
import com.example.studyrestapi.demo.accounts.AccountRole;
import com.example.studyrestapi.demo.accounts.AccountService;
import com.example.studyrestapi.demo.common.AppProperties;
import com.example.studyrestapi.demo.common.BaseControllerTest;
import com.example.studyrestapi.demo.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws  Exception{
        //Given
        String username = appProperties.getAdminUsername();
        String password = appProperties.getAdminPassword();
//        Account account = Account.builder()
//                            .email(username)
//                            .password(password)
//                            .roles(Stream.of(AccountRole.ADMIN, AccountRole.USER).collect(Collectors.toSet()))
//                            .build();
//        this.accountService.saveAccount(account);

        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();

        //When & Then
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret)) //basicAuth header 생성
                    .param("username", username)
                    .param("password",password)
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }

}
