package com.example.studyrestapi.demo.configs;

import com.example.studyrestapi.demo.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    //SecurityConfig에서 bean으로 설정한 것 가지고 온다. 유저 정보를 얻기 위해서
    @Autowired
    AuthenticationManager authenticationManager;


    @Autowired
    TokenStore tokenStore;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //client의 secret 인코딩하여 확인
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //inmemory에 저장하겠다. jdbc로 바꿔줘도됨
        clients.inMemory()
                .withClient("myApp")
                .authorizedGrantTypes("password","refresh_token") //password, refresh_token 방식 사용하겠다.
                .scopes("read", "write")//사용자가 정하기 나름
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60)//10분
                .refreshTokenValiditySeconds(6 * 10 * 60)//1시간
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore);

    }

}
