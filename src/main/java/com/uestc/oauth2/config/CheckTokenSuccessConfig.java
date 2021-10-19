package com.uestc.oauth2.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/10/18 17:20
 * @description:
 */
@RestController
public class CheckTokenSuccessConfig {
    @GetMapping("oauth/check_token")
    public Object checkToken(String token){


    }
}
