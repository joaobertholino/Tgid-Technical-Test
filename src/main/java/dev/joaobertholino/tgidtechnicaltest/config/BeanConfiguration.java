package dev.joaobertholino.tgidtechnicaltest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class BeanConfiguration {

	@Bean
	public RestTemplate restTemplateBeanCreation() {
		return new RestTemplateBuilder().build();
	}

	@Bean
	public OpenAPI openAPI() {
		Server server = new Server();
		server.setUrl("http://localhost:8080/");
		server.setDescription("Default");

		Contact contact = new Contact()
				.name("João Bertholino")
				.email("comercial.bertholino@gmail.com");

		Info info = new Info()
				.title("Technical Test - TGID")
				.description("Transaction API between Customer and Company integrating WebHook services and sending emails.")
				.version("1.0")
				.contact(contact);
		return new OpenAPI().info(info).servers(List.of(server));
	}
}
