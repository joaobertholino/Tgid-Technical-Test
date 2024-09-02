package dev.joaobertholino.tgidtechnicaltest.service.exception.handler;

import dev.joaobertholino.tgidtechnicaltest.service.exception.ClientNotFound;
import dev.joaobertholino.tgidtechnicaltest.service.exception.EnterpriseNotFound;
import dev.joaobertholino.tgidtechnicaltest.service.exception.exceptiontemplate.ExceptionTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@ControllerAdvice
public class EntityExceptionHandler {

	@Value(value = "${webhook.url}")
	private String webhookUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	@ExceptionHandler(EnterpriseNotFound.class)
	private ResponseEntity<ExceptionTemplate> enterpriseNotFound(EnterpriseNotFound e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.NOT_FOUND;
		String error = "Enterprise entity not found";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}

	@ExceptionHandler(ClientNotFound.class)
	private ResponseEntity<ExceptionTemplate> clientNotFound(ClientNotFound e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.NOT_FOUND;
		String error = "Client entity not found";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}
}
