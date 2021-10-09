package com.uestc.oauth2.config;

import com.uestc.oauth2.utils.Md5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 17:00
 */
@Component
public class Md5Password implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Md5Utils.generate(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return Md5Utils.verify(charSequence.toString(),s);
    }
}
