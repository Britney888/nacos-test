package com.example.nacosconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/*@ComponentScan("com.example.nacosconsumer.controller")*/
@EnableFeignClients("com.example.democlient.client")
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }
    @Bean
    @LoadBalanced //开启负载均衡
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
