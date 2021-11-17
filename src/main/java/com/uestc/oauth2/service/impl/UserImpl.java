package com.uestc.oauth2.service.impl;

import com.uestc.oauth2.entity.User;
import com.uestc.oauth2.mapper.UserMapper;
import com.uestc.oauth2.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 16:03
 */
@Service
public class UserImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(s);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        return user;
    }

    @Override
    public User findByFpCode(String fpCode) throws UsernameNotFoundException {
        User user = userMapper.loadUserByFingerprint(fpCode);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("指纹不正确");
        }
        return user;
    }

    @Override
    public User findByUKey(String uKey) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUKey(uKey);
        if(ObjectUtils.isEmpty(user)){
            throw new UsernameNotFoundException("UKey不正确");
        }
        return user;
    }
}
