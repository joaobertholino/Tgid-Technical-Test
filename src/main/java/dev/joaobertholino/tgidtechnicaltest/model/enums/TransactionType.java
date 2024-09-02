package dev.joaobertholino.tgidtechnicaltest.model.enums;

public enum TransactionType {
	DEPOSIT("Deposit"),
	WITHDRAWAL("Withdrawal");

	private final String transactionTypeName;

	TransactionType(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}

	public String getTransactionTypeName() {
		return transactionTypeName;
	}
}
