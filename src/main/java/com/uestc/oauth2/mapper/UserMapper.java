package com.uestc.oauth2.mapper;

import com.uestc.oauth2.entity.User;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 17:18
 */
public interface UserMapper {

    /**
     * 根据用户名称查询用户
     *
     * @param username 用户名称
     * @return 查询结果
     */
    User findByUsername(String username);

    /**
     *
     * @param fpCode
     * @return 查询结果
     */
    User findByFpCode(String fpCode);

    /**
     *
     * @param uKey
     * @return 查询结果
     */
    User findByUKey(String uKey);

}
