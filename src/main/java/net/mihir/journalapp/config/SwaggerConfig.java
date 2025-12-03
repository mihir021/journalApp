package net.mihir.journalapp.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI journalAppAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📓 JournalApp API")
                        .description("Secure journaling backend with JWT, MongoDB & Spring Boot.")
                        .version("1.0.0")
                        .contact(new Contact().name("Mihir Rathod").url("https://github.com/mihir021"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("http://localhost:8082").description("Production")
                ))
                .tags(List.of(
                        new Tag().name("Public APIs").description("Signup & Login (No Auth)"),
                        new Tag().name("User APIs").description("Manage user profile & settings"),
                        new Tag().name("Journal APIs").description("CRUD for journal entries"),
                        new Tag().name("Admin APIs").description("Admin controls & analytics")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")));
    }
}
