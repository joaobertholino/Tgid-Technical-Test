package dev.joaobertholino.tgidtechnicaltest.service.exception;

public class EnterpriseNotFound extends RuntimeException {
	public EnterpriseNotFound() {
	}

	public EnterpriseNotFound(String message) {
		super(message);
	}
}
