package dev.joaobertholino.tgidtechnicaltest.service.exception;

public class TransactionValueInvalid extends RuntimeException {
	public TransactionValueInvalid() {}
	public TransactionValueInvalid(String message) {
		super(message);
	}
}
