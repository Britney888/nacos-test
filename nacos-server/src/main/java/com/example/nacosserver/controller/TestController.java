package com.example.nacosserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author ning
 * @Date 2021/3/2 9:59
 */
@Slf4j
@Controller
@RestController
@RefreshScope
public class TestController {
    @Value(value = "${fileServerBaseUrl}")
    private String URL;
    @GetMapping("hello")
    public String test(){
        return this.URL;
    }

    @PostMapping("/getInfo")

    String getOpenMesg(@RequestParam("name")String name){
        log.info("请求参数{}",name);
        String mesg="用户"+name+"获取开放平台信息地址:"+URL;
        return mesg;
    }
}

