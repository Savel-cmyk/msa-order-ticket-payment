package com.travel.application.gatewayservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI gatewayServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway service")
                        .description("API Gateway service's Swagger endpoint as a shortcut for application's services" +
                                " endpoints documentation")
                        .version("v0.0.1"));
    }
}
