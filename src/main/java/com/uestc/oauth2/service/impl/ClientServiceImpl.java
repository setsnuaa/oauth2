package com.uestc.oauth2.service.impl;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 16:52
 */
public class ClientServiceImpl extends JdbcClientDetailsService {

    public ClientServiceImpl(DataSource dataSource) {
        super(dataSource);
    }
}
