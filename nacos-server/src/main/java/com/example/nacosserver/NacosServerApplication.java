package com.example.nacosserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients("com.example.democlient.client")//消费者
@SpringBootApplication
public class NacosServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }

}
