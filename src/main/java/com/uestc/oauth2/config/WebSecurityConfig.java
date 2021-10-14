package com.uestc.oauth2.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 16:09
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /***
     *
     * 功能描述: 如果不配置 springboot会自动配置一个覆盖掉 内存中的用户
     * @return org.springframework.security.authentication.AuthenticationManager
     * @author qwp
     * @date 2021/4/12 16:58
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/token", "/oauth/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }


}