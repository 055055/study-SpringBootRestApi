package com.example.studyrestapi.demo.configs;

import com.example.studyrestapi.demo.accounts.Account;
import com.example.studyrestapi.demo.accounts.AccountRole;
import com.example.studyrestapi.demo.accounts.AccountService;
import com.example.studyrestapi.demo.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class AppConfig {


    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                //Given
                Set<AccountRole> role = new HashSet<AccountRole>();
                role.add(AccountRole.ADMIN);
                role.add(AccountRole.USER);

                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(role)
                        .build();
                this.accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Stream.of(AccountRole.USER).collect(Collectors.toSet()))
                        .build();
                this.accountService.saveAccount(user);



            }
        };

    }


}
