package com.uestc.oauth2.source.provide;

import com.uestc.oauth2.entity.User;
import com.uestc.oauth2.service.UserService;
import com.uestc.oauth2.source.token.FingerPrintToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/3 15:47
 * @description:指纹认证提供者,对认证逻辑的验证，比如账密校验用户名和密码是否匹配，指纹校验指纹是否匹配
 */
public class FingerPrintProvide implements AuthenticationProvider {
    private UserService userDetailsService;

    public FingerPrintProvide() {
    }

    public FingerPrintProvide(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authenticationToken) throws AuthenticationException {
        // 传入参数为指纹（待定）
        //String username = (String) authenticationToken.getPrincipal();
        String fingerprint = (String) authenticationToken.getPrincipal();
        User user = userDetailsService.findByFpCode(fingerprint);
        FingerPrintToken authenticationResult = new FingerPrintToken(user, fingerprint, null);
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return  FingerPrintToken.class.isAssignableFrom(aClass);
    }
}
