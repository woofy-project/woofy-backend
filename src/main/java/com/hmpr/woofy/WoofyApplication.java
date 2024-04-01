package com.hmpr.woofy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Woofy API",
				version = "0.0"
		),
		servers = @Server(url = "http://localhost:8080")
)
public class WoofyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoofyApplication.class, args);
	}

}
