package dev.joaobertholino.tgidtechnicaltest.service.exception.handler;

import dev.joaobertholino.tgidtechnicaltest.service.exception.exceptiontemplate.ExceptionTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@ControllerAdvice
public class MailExceptionHandler {
	private final RestTemplate restTemplate;

	@Value(value = "${webhook.url}")
	private String webhookUrl;

	public MailExceptionHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@ExceptionHandler(MailAuthenticationException.class)
	private ResponseEntity<ExceptionTemplate> mailAuthenticationException(MailAuthenticationException e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String error = "Authentication failed";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}

	@ExceptionHandler(MailSendException.class)
	private ResponseEntity<ExceptionTemplate> mailSendException(MailSendException e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		String error = "Mail connection failed";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}

	@ExceptionHandler(MailParseException.class)
	private ResponseEntity<ExceptionTemplate> mailParseException(MailParseException e, HttpServletRequest servletRequest) {
		LocalDateTime timestamp = LocalDateTime.now();
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		String error = "Mail message parse failed";
		ExceptionTemplate exceptionTemplate = new ExceptionTemplate(timestamp, status.value(), error, e.getMessage(), servletRequest.getRequestURI());

		this.restTemplate.postForEntity(this.webhookUrl, exceptionTemplate, String.class);
		return ResponseEntity.status(status).build();
	}
}
