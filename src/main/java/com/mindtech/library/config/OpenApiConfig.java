package com.mindtech.library.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library App API")
                        .description("Publisher Management Prototype API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MindTech")
                                .email("info@mindtech.com")));
    }

    @Bean
    public OperationCustomizer pageableOperationCustomizer() {
        return (operation, handlerMethod) -> {
            var hasPageable = Arrays.stream(handlerMethod.getMethodParameters())
                    .anyMatch(param -> Pageable.class.isAssignableFrom(param.getParameterType()));

            if (!hasPageable) {
                return operation;
            }

            if (operation.getParameters() != null) {
                operation.getParameters().removeIf(param -> {
                    var name = param.getName();
                    return "pageable".equals(name)
                            || "page".equals(name)
                            || "size".equals(name)
                            || "sort".equals(name);
                });
            }

            var pageParam = new Parameter()
                    .in(ParameterIn.QUERY.toString())
                    .name("page")
                    .description("Page number (0-based)")
                    .schema(new IntegerSchema()._default(0));

            var sizeParam = new Parameter()
                    .in(ParameterIn.QUERY.toString())
                    .name("size")
                    .description("Page size")
                    .schema(new IntegerSchema()._default(20));

            var sortParam = new Parameter()
                    .in(ParameterIn.QUERY.toString())
                    .name("sort")
                    .description("Sort: property,direction (e.g. id,desc)")
                    .schema(new StringSchema().example("id,asc"));

            operation.addParametersItem(pageParam);
            operation.addParametersItem(sizeParam);
            operation.addParametersItem(sortParam);

            return operation;
        };
    }
}