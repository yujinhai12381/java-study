
package com.jinhai.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class PayMentMain {
    public static void main(String[] args) {
        SpringApplication.run(PayMentMain.class, args);
    }
}