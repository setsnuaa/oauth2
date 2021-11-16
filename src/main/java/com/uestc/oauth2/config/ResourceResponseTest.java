package com.uestc.oauth2.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name:
 * @author:pan.gefei
 * @date:2021/11/16 16:05
 * @description:
 */
@RestController
@RequestMapping("user")
public class ResourceResponseTest {
    @GetMapping("/test")
    public String test(/*@RequestParam("token")String accessToken*/){
        //调用check_token接口，查看token是否合法

        //合法返回ok
        return "ok";

        //不合法返回fail
    }
}
