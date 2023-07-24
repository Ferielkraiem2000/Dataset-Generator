package com.datasetgenerator.annotationtool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Annotation Tool API Dataset Generator",
                version = "1.0",
                description = "API for the Dataset Generator Annotation Tool"
        ))
public class SwaggerConfig {

}


