package com.cashout.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashoutOpenApi {

    @Bean
    public OpenAPI cashoutOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cashout API")
                        .version("1.0")
                        .description("Cashout API for managing users and transactions"));
    }
}
