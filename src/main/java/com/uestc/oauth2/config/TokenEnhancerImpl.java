package com.uestc.oauth2.config;

import com.uestc.oauth2.entity.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @name:
 * @author:Setsnua
 * @date:2021/10/8 17:24
 * @description:token新增字段
 */
@Component
public class TokenEnhancerImpl implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        DefaultOAuth2AccessToken token=(DefaultOAuth2AccessToken)oAuth2AccessToken;
        User user = (User)oAuth2Authentication.getPrincipal();
        Map<String,Object> map=oAuth2AccessToken.getAdditionalInformation();
        map.put("userid",user.getId());//添加user id
        token.setAdditionalInformation(map);
        return oAuth2AccessToken;
    }
}
