package org.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.example.apigateway.filter.AuthHeaderFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-member", r -> r.path("/members/**")
                        .uri("lb://user-service"))

                .route("user-admin", r -> r.path("/admin/**")
                        .uri("lb://user-service"))

                .route("user-auth", r -> r.path("/auth/**")
                        .uri("lb://user-service"))

                .route("track-service", r -> r.path("/tracks/**")
                        .uri("lb://track-service"))

                .build();
    }
}