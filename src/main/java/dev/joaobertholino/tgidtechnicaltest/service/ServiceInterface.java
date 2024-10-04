package dev.joaobertholino.tgidtechnicaltest.service;

import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface ServiceInterface {

	@Transactional
	void carryOutTransaction(Integer enterpriseId, Integer clientId, BigDecimal value, TransactionType transactionType);
}
