package com.uestc.oauth2.config;

import java.util.HashMap;


/**
 * @name:
 * @author:pan.gefei
 * @date:2021/10/8 15:20
 * @description:
 */
public class ResponseResult extends HashMap<String, Object> {

    public ResponseResult() {
        this.put("code", 0);
        this.put("msg", "false");
    }


    public static ResponseResult ok() {
        ResponseResult result = new ResponseResult();
        result.put("code", 200);
        result.put("msg", "操作成功");//覆盖
        return result;
    }

    public ResponseResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}

