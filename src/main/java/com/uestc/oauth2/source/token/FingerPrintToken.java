package com.uestc.oauth2.source.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/3 15:42
 * @description:指纹认证
 */
public class FingerPrintToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     *
     */
    public FingerPrintToken(String fpCode) {
        super(null);
        this.principal=fpCode;
        //this.credentials = fpCode;
        setAuthenticated(false);
    }

    /**
     * 该构造函数主要是对认证的使用是认证成功之后
     * 首先principal会换成我们项目中的用户entity
     * authorities则是用户权限 如果没有或没有使用到为null也行
     */
    public FingerPrintToken(Object principal, Object credentials,
                    Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
