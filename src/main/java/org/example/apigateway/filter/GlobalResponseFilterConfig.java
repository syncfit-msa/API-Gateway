package org.example.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class GlobalResponseFilterConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public GlobalFilter globalResponseWrapper() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.defer(() -> {
            if (!exchange.getResponse().getStatusCode().is2xxSuccessful()) {
                return Mono.empty(); // 에러는 다른 필터에서 처리
            }

            // 응답 타입이 JSON일 때만 처리
            if (!MediaType.APPLICATION_JSON.equals(exchange.getResponse().getHeaders().getContentType())) {
                return Mono.empty();
            }

            // Body는 아래 필터에서 처리
            return Mono.empty();
        }));
    }

    @Bean
    public ModifyResponseBodyGatewayFilterFactory.Config globalBodyFormatter() {
        return new ModifyResponseBodyGatewayFilterFactory.Config()
                .setRewriteFunction(Object.class, Object.class, (exchange, originalBody) -> {
                    HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
                    Map<String, Object> wrapped = Map.of(
                            "success", true,
                            "status", status != null ? status.value() : 200,
                            "data", originalBody,
                            "timestamp", LocalDateTime.now()
                    );
                    return Mono.just(wrapped);
                });
    }
}