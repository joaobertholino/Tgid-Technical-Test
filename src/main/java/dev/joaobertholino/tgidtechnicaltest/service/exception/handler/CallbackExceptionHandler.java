package dev.joaobertholino.tgidtechnicaltest.service.exception.handler;

import dev.joaobertholino.tgidtechnicaltest.service.exception.exceptiontemplate.ExceptionTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@ControllerAdvice
public class CallbackExceptionHandler {

	@Value(value = "${webhook.url}")
	private String webhookUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	@ExceptionHandler(RestClientException.class)
	private ResponseEntity<ExceptionTemplate> restClientException(RestClientException e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.FAILED_DEPENDENCY;
		String error = "Rest client failed";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}
}
