package dev.joaobertholino.tgidtechnicaltest.service;

import dev.joaobertholino.tgidtechnicaltest.controller.model.request.Request;
import jakarta.transaction.Transactional;

public interface ServiceInterface {

	@Transactional
	void carryOutTransaction(Request request);
}
