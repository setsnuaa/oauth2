package com.uestc.oauth2.config;

import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.util.List;
/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/2 13:52
 * @description:加入自定义认证模式
 */
@Data
public class ExtendedTokenGranter implements TokenGranter {
    // 源码中的认证模式构造器
    private CompositeTokenGranter delegate;

    // 认证集合
    private List<TokenGranter> tokenGranters;


    // 这里代码与源码中的无异
    @Override
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        if (delegate == null) {
            delegate = new CompositeTokenGranter(this.tokenGranters);
        }
        OAuth2AccessToken grant;
        grant = delegate.grant(grantType, tokenRequest);
        return grant;
    }
}
