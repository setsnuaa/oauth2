package com.uestc.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/9/29 16:08
 * @description:
 */
@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/token", "/oauth/logout","/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
