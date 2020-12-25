package com.example.studyrestapi.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
     //외부 요청이 리소스에 접근 시 AuthServer에서 인증받은 토큰을 가지고와서 토큰이 유효한지 확인하여 접근제어를 하는 역할
    //ResourceServer는 비즈니스 로직을 수행하는 서버에 있는 것이 맞고, AuthServer는 같이 있어도 되지만 엄밀히 말하면 따로 두는게 좋다.


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .anonymous()
                .and()
            .authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/api/**")
                    .anonymous()
                .anyRequest()
                    .authenticated()
                .and()
            .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
        // 접근제어 관련하여 에러가 나왔을 때는 Oauth2AccessDeniedHander가 처리할 것이다.

    }
}
