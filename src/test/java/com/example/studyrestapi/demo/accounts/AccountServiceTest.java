package com.example.studyrestapi.demo.accounts;

import com.example.studyrestapi.demo.common.AppProperties;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.useDefaultDateFormatsOnly;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AppProperties appProperties;

    @Test
    public void findByUsername(){
        //Given
//        Set<AccountRole> role = new HashSet<AccountRole>();
//        role.add(AccountRole.ADMIN);
//        role.add(AccountRole.USER);
//
        String password = appProperties.getAdminPassword();
        String username = appProperties.getAdminUsername();
//        Account account = Account.builder()
//                                .email(username)
//                                .password(password)
//                                .roles(role)
//                                .build();
//        this.accountService.saveAccount(account);

        //When
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);



        //Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();

    }


    @Test
    public void findByUsernameFail(){
        //Expected
        //미리 발생할 예외 정의
        String username = "random@email.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        //When
        accountService.loadUserByUsername(username);


    }

}
