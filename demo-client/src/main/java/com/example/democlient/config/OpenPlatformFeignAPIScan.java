package com.example.democlient.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import static com.example.democlient.config.OpenPlatformFeignAPIScan.OPEN_CLIENT_PACKAGES;


/**
 * 扫描 {@link FeignClient} 标记的Api接口
 *
 * @author <a href="mailto:wangchenglong@weibopay.com">wangchenglong</a>
 * @date 2020年04月16日
 */
@Component
@EnableFeignClients(OPEN_CLIENT_PACKAGES)
@ComponentScan(OPEN_CLIENT_PACKAGES)
class OpenPlatformFeignAPIScan {
    /**
     * 扫描的包地址
     */
    public static final String OPEN_CLIENT_PACKAGES = "com.example.democlient.client";
}
