package com.uestc.oauth2.config;

import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @name:
 * @author:Setsnua
 * @date:2021/10/14 16:56
 * @description:
 */
@RestController
public class LogoutSuccessConfig {
    @Resource
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping("/oauth/logout")
    public Object logout(@RequestParam("token") String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return LogoutResult.ok();
        }
        return LogoutResult.fail();
    }
}
