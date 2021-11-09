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
import lombok.SneakyThrows;

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
    private ClientServiceImpl clientDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端配置
        clients.jdbc(dataSource).passwordEncoder(md5Password);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //配置令牌端点的安全约束，这个端点谁能访问，谁不能访问
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置令牌的访问端点和令牌服务
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
     * 使用 oauth默认授权码模式
     *
     * @return oauth默认授权码模式
     */
    private AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 采用默认的工厂
     *
     * @return 请求工厂
     */
    private OAuth2RequestFactory requestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
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
     * 授权模式注入IOC
     */
    @Bean
    public TokenGranter tokenGranter() {
        ExtendedTokenGranter tokenGranter = new ExtendedTokenGranter();
        // 这里使用set方法 将所有的TokenGranter都添加到CompositeTokenGranter的构造函数里
        tokenGranter.setTokenGranters(getAllTokenGranters());
        return tokenGranter;
    }

    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerTokenServices tokenServices
            , AuthorizationCodeServices authorizationCodeServices, OAuth2RequestFactory requestFactory) {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        // 添加授权码模式
        tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory));
        // 添加刷新令牌的模式
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
        // 添加隐士授权模式
        tokenGranters.add(new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory));
        // 添加客户端模式
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
        if (authenticationManager != null) {
            // 添加密码模式
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,clientDetailsService, requestFactory));
            // 添加UKey模式
        }
        return tokenGranters;
    }

    /**
     * 所有授权模式：默认的5种模式 + 自定义的模式
     */
    private List<TokenGranter> getAllTokenGranters() {
        // 获取新的 认证服务配置
        AuthorizationServerTokenServices tokenServices = tokenServices();
        // 授权码模式配置
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        // 认证请求工厂
        OAuth2RequestFactory requestFactory = requestFactory();
        //获取默认的授权模式
        List<TokenGranter> tokenGranters = getDefaultTokenGranters(tokenServices, authorizationCodeServices, requestFactory);
        if (authenticationManager != null) {
            // 自己的认证模式  构造函数最后一个形参 则是grant_type的参数值
            // 指纹认证
            tokenGranters.add(new FingerPrintGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory));
            // ukey认证
            //tokenGranters.add(new OpenIdAuthGranter(authenticationManager,tokenServices,clientDetailsService,requestFactory,"weChat"));
        }
        return tokenGranters;
    }


    private AuthorizationServerTokenServices tokenServices() {
        // token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        // 设置认证返回
        tokenEnhancers.add(tokenEnhancer);
        // 设置token JWT
        tokenEnhancers.add(jwtAccessTokenConverter);
        // 设置token增强器
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        return defaultTokenServices;
    }

}
