package org.example.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ApigatewayApplication {

    @Value("${eureka.client.service-url.defaultZone}")
    private String url;

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        log.info("Task 환경 변수 - S3에 정의된 env 파일 주입");
        log.info("---------------EUREKA URL ------------ {}", url);
    }

}
