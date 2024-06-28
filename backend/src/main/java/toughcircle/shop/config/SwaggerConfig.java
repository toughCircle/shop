package toughcircle.shop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "Shop API",
    description = "쇼핑몰 API 명세서입니다.",
    version = "v1")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openApi() {
        // "/api/**" 경로 매칭 api 그룹화
        String[] path = {"/api/**"};

        return GroupedOpenApi.builder()
            .group("Shop API")
            .pathsToMatch(path)
            .build();
    }
}
