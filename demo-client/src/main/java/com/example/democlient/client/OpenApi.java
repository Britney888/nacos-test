package com.example.democlient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description TODO
 * @Author ning
 * @Date 2021/3/9 15:53
 */
@FeignClient(value = "nacos-server")
public interface OpenApi {
   @PostMapping("/getInfo")
   String getOpenMesg(@RequestParam("name") String name);
}
