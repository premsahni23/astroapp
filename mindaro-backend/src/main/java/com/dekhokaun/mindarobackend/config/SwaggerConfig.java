package com.dekhokaun.mindarobackend.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer filterOnlyApiPaths() {
        return openApi -> openApi.getPaths().keySet()
                .removeIf(path -> !path.startsWith("/api"));
    }
}
