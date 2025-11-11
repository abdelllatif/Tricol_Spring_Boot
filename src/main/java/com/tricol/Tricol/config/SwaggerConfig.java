package com.tricol.Tricol.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI tricolOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tricol API")
                        .description("CRUD operations for suppliers and products")
                        .version("v1.0")
                        .contact(new Contact().name("Abdi Hissoune").email("you@example.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Tricol Project Wiki Documentation")
                        .url("https://example.com/docs"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("tricol-public")
                .pathsToMatch("/**")
                .build();
    }
}
