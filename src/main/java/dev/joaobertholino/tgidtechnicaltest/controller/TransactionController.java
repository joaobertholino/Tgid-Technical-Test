package dev.joaobertholino.tgidtechnicaltest.controller;

import dev.joaobertholino.tgidtechnicaltest.controller.requesttemplate.Request;
import dev.joaobertholino.tgidtechnicaltest.service.ServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
	private final ServiceInterface serviceInterface;

	public TransactionController(ServiceInterface serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@PostMapping()
	public ResponseEntity<Void> carryOutTransaction(@RequestBody Request request) {
		this.serviceInterface.carryOutTransaction(request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
