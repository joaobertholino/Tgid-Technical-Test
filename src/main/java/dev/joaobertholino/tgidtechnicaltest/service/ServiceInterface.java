package dev.joaobertholino.tgidtechnicaltest.service;

import dev.joaobertholino.tgidtechnicaltest.controller.requesttemplate.Request;
import jakarta.transaction.Transactional;

public interface ServiceInterface {

	@Transactional
	void carryOutTransaction(Request request);
}
