package gorshkov.profi.software.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@AllArgsConstructor
public class SwaggerConfig {

    @Bean
    public Docket externalApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("gorshkov.profi.software.controller"))
                .paths(PathSelectors.regex("/.{1,}"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .groupName("default");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "RESTful API сервис работы с заметками", "Я профи, программная инженерия, полуфинал",
                "1.0", null, null, null, null, Collections.emptyList());
    }
}
