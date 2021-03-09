package com.example.nacosclient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description TODO
 * @Author ning
 * @Date 2021/3/9 15:53
 */
@FeignClient(value = "nacos-server")
public interface OpenApi {
   @PostMapping("/getInfo")
   String getOpenMesg(String name);
}
