package com.example.studyrestapi.demo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter @Setter
public class AppProperties {
    //ConfigurationProperties를 사용하면 springEl을 사용하여 @value를 하는 것보다 더 타입세이프 하게 쓸 수 있다.
    // 컴파일 과정에서 불러올 때 체크 하기 때문에.

    @NotEmpty
    private String adminUsername;

    @NotEmpty
    private String adminPassword;

    @NotEmpty
    private String userUsername;

    @NotEmpty
    private String userPassword;

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;


}
