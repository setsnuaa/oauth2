package com.uestc.oauth2.config;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/10/14 16:56
 * @description:
 */
@RestController
public class LogoutSuccessConfig {
    @Resource
    private ConsumerTokenServices consumerTokenServices;

    /**
     * 退出登录并删除redis中的token
     * @param accessToken
     * @return
     */
    @GetMapping("/oauth/logout")
    public Object logout(@RequestParam("token")String accessToken) {
        if (consumerTokenServices.revokeToken(accessToken)) {
            return LogoutResult.ok();
        }
        return LogoutResult.fail();
    }

}
