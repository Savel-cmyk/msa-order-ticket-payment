package com.travel.application.paymentservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Value("${GATEWAY_SERVER_PORT}")
    private String port;

    @Bean
    public OpenAPI paymentServiceAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:" + port)))
                .info(new Info()
                        .title("Payment service API")
                        .version("v0.0.1"));
    }
}
