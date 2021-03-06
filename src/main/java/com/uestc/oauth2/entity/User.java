package com.uestc.oauth2.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author jie.zhong
 * @version 1.0
 * @date 2021/9/22 17:14
 */
@Data
public class User implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private String fingerprint;

    private String ukey;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUkey(){return this.ukey;}

    public String getFingerprint(){return this.fingerprint;}

    public Long getId() {
        return this.id;
    }
}
