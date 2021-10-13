package com.uestc.oauth2.config;


import com.uestc.oauth2.service.UserService;
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
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

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

    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //services.setClientDetailsService(clientDetailsService());
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter(), tokenEnhancer));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }

}
