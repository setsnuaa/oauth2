package com.uestc.oauth2.source.config;

import com.uestc.oauth2.service.UserService;
import com.uestc.oauth2.source.provide.FingerPrintProvide;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import javax.annotation.Resource;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/3 15:36
 * @description:
 */
public class FingerPrintConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource
    private UserService userDetailsService;


    @Override
    public void configure(HttpSecurity http) {
        FingerPrintProvide provider = new FingerPrintProvide(userDetailsService);
        http.authenticationProvider(provider);
    }
}
