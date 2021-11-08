package com.uestc.oauth2.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.uestc.oauth2.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 16:03
 */
public interface UserService extends UserDetailsService {
    /**
     *
     * @param fpCode
     * @return 用户
     * @throws UsernameNotFoundException
     */
    User findByFpCode(String fpCode) throws UsernameNotFoundException;

    /**
     *
     * @param uKey
     * @return 用户
     * @throws UsernameNotFoundException
     */
    User findByUKey(String uKey) throws UsernameNotFoundException;
}
