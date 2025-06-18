package com.vcarrin87.jdbc_example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    /**
     * OpenAPI configuration bean.
     *
     * @return OpenAPI instance with API information.
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Jdbc API")
                .version("1.0")
                .description("API documentation")
                .contact(new Contact().name("Valentina Carrington").email("vcarrin87@gmail.com")));
    }
}
