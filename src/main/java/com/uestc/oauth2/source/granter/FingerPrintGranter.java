package com.uestc.oauth2.source.granter;

import com.uestc.oauth2.source.token.FingerPrintToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/3 15:37
 * @description:
 */
public class FingerPrintGranter extends AbstractTokenGranter {
    private AuthenticationManager manager;

    public FingerPrintGranter(AuthenticationManager manager, AuthorizationServerTokenServices tokenServices,
                              ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.manager = manager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        //String username = parameters.get("username");
        String fpCode = parameters.get("fpCode");
        parameters.remove("fpCode");
        Authentication userAuth = new FingerPrintToken(fpCode);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = manager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate fingerprint: " + fpCode);
        }
        OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(oAuth2Request, userAuth);
    }
}
