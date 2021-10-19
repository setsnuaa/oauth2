package com.uestc.oauth2.config;

import java.util.HashMap;
import java.util.Objects;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/10/14 18:43
 * @description:
 */
public class LogoutResult extends HashMap<String, Object> {
    public LogoutResult(){
        this.put("code",0);
        this.put("msg","操作失败");
        this.put("data",null);
    }

    public static LogoutResult ok(){
        LogoutResult logoutResult = new LogoutResult();
        logoutResult.put("code",200);
        logoutResult.put("msg","操作成功");
        return logoutResult;
    }

    public static LogoutResult fail(){
        LogoutResult logoutResult = new LogoutResult();
        return logoutResult;
    }
}
