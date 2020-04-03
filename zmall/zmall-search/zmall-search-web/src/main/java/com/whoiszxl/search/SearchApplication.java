package com.whoiszxl.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @description: 搜索服务启动类
 * @author: whoiszxl
 * @create: 2020-03-19
 **/
@ComponentScan(basePackages = {"com.whoiszxl.search", "com.whoiszxl.common.exception"})
@SpringBootApplication
@MapperScan("com.whoiszxl.search.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.whoiszxl.product.feign"})
@EnableScheduling
public class SearchApplication {


    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }
}
