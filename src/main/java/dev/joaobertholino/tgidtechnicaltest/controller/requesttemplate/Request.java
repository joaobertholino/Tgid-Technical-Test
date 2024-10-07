package dev.joaobertholino.tgidtechnicaltest.controller.requesttemplate;

import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;

import java.math.BigDecimal;

public record Request(Integer enterpriseId, Integer clientId, BigDecimal value, TransactionType transactionType) {
}
