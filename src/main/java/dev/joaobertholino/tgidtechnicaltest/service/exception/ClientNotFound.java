package dev.joaobertholino.tgidtechnicaltest.service.exception;

public class ClientNotFound extends RuntimeException {
	public ClientNotFound() {
	}

	public ClientNotFound(String message) {
		super(message);
	}
}
