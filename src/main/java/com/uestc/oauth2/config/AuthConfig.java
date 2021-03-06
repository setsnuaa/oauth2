package com.uestc.oauth2.config;


import com.uestc.oauth2.service.UserService;
import com.uestc.oauth2.service.impl.ClientServiceImpl;
import com.uestc.oauth2.source.granter.FingerPrintGranter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 15:26
 */
@Configuration
@EnableAuthorizationServer
@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private DataSource dataSource;

    @Resource
    private Md5Password md5Password;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private TokenEnhancerImpl tokenEnhancer;

    @Resource
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource
    private ClientServiceImpl clientService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //???????????????
        clients.jdbc(dataSource).passwordEncoder(md5Password);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //??????????????????????????????????????????????????????????????????????????????
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //??????????????????????????????????????????
        endpoints.userDetailsService(userService);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), tokenEnhancer));
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), tokenEnhancer));
        endpoints.authenticationManager(authenticationManager)
                .reuseRefreshTokens(true)
                .tokenStore(tokenStore)
                //.accessTokenConverter(jwtAccessTokenConverter());
                .tokenEnhancer(tokenEnhancerChain);

    }

    @Bean
    public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("uestc");
        return jwtAccessTokenConverter;
    }

    /**
     * ?????? oauth?????????????????????
     *
     * @return oauth?????????????????????
     */
    private AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * ?????????????????????
     *
     * @return ????????????
     */
    private OAuth2RequestFactory requestFactory() {
        return new DefaultOAuth2RequestFactory(clientService);
    }

/*    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //services.setClientDetailsService(clientDetailsService());
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), tokenEnhancer));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }*/

    /**
     * ??????????????????IOC
     */
    @Bean
    public TokenGranter tokenGranter() {
        ExtendedTokenGranter tokenGranter = new ExtendedTokenGranter();
        // ????????????set?????? ????????????TokenGranter????????????CompositeTokenGranter??????????????????
        tokenGranter.setTokenGranters(getAllTokenGranters());
        return tokenGranter;
    }

    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerTokenServices tokenServices
            , AuthorizationCodeServices authorizationCodeServices, OAuth2RequestFactory requestFactory) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        // ?????????????????????
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientService, requestFactory));
        // ???????????????????????????
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientService, requestFactory));
        // ????????????????????????
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientService, requestFactory));
        // ?????????????????????
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientService, requestFactory));
        if (authenticationManager != null) {
            // ??????????????????
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientService, requestFactory));
            // ??????UKey??????
        }
        return tokenGranters;
    }

    /**
     * ??????????????????????????????5????????? + ??????????????????
     */
    private List<TokenGranter> getAllTokenGranters() {
        // ???????????? ??????????????????
        AuthorizationServerTokenServices tokenServices = tokenServices();
        // ?????????????????????
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        // ??????????????????
        OAuth2RequestFactory requestFactory = requestFactory();
        //???????????????????????????
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(tokenServices, authorizationCodeServices, requestFactory);
        if (authenticationManager != null) {
            // ?????????????????????  ?????????????????????????????? ??????grant_type????????????
            // ????????????
            tokenGranters.add(new FingerPrintGranter(authenticationManager, tokenServices, clientService, requestFactory));
            // ukey??????
            //tokenGranters.add(new OpenIdAuthGranter(authenticationManager,tokenServices,clientDetailsService,requestFactory,"weChat"));
        }
        return tokenGranters;
    }


    private AuthorizationServerTokenServices tokenServices() {
        // token??????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        // ??????????????????
        tokenEnhancers.add(tokenEnhancer);
        // ??????token JWT
        tokenEnhancers.add(jwtAccessTokenConverter);
        // ??????token?????????
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setClientDetailsService(clientService);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }

}
