package net.dudko.microservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@OpenAPIDefinition(
		info = @Info(
				title = "Staff service",
				description = "Staff service REST API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Dudko Anatol",
						email = "anatoly_dudko@icloud.com"
				),
				license = @License(
						name = "GPL-3.0 license",
						url = "https://github.com/aDudko/moneron?tab=GPL-3.0-1-ov-file"
				)
		)
)
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class StaffServiceApplication {

//	@Bean
//	public RestTemplate restTemplate() {
//		return new RestTemplate();
//	}

//	@Bean
//	public WebClient webClient() {
//		return WebClient.builder().build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(StaffServiceApplication.class, args);
	}

}
