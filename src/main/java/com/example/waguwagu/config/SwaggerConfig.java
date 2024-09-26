package com.example.waguwagu.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final static String AUTH_HEADER = "Authorization";

    @Bean
    public OpenAPI openApi() {
        var securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat(AUTH_HEADER)
                .in(SecurityScheme.In.HEADER)
                .name(AUTH_HEADER);

        var addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList(AUTH_HEADER);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(AUTH_HEADER, securityScheme))
                .addSecurityItem(addSecurityItem)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("와구와구 배달 기사 API")
                .description("API 사용 메뉴얼")
                .version("0.0.1");
    }
}
