package com.example.UploadFile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title="Dataset Generator Project"))

public class UploadFileApplication {
	public static void main(String[] args) {
		SpringApplication.run(UploadFileApplication.class, args);
	}

}
