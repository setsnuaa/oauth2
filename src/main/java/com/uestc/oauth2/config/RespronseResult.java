package com.uestc.oauth2.config;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @name:
 * @author:Setsnua
 * @date:2021/10/8 15:20
 * @description:
 */
public class RespronseResult extends HashMap<String,Object> {

    public RespronseResult(){
        this.put("code",0);
        this.put("msg","false");
    }


    public static RespronseResult ok() {
        RespronseResult result=new RespronseResult();
        result.put("code",200);
        result.put("msg","操作成功");//覆盖
        return result;
    }

    public RespronseResult put(String key,Object value){
        super.put(key,value);
        return this;
    }

}

