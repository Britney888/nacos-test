package com.example.nacosconsumer.controller;

import com.example.democlient.client.OpenApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author ning
 * @Date 2021/3/9 16:13
 */
@Slf4j
@RestController
@Controller
public class TestController {

    @Resource
    OpenApi openApi;
    @PostMapping("/test")
    String test(@RequestParam("name")String name){
        log.info("请求参数{}",name);
        return openApi.getOpenMesg(name);
    }


}
