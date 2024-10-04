package dev.joaobertholino.tgidtechnicaltest.controller;

import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import dev.joaobertholino.tgidtechnicaltest.service.ServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {
	private final ServiceInterface serviceInterface;

	public TransactionController(ServiceInterface serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@PostMapping()
	public ResponseEntity<Void> carryOutTransaction(@RequestParam Integer enterpriseId, @RequestParam Integer clientId, @RequestParam BigDecimal value, @RequestParam TransactionType transactionType) {
		this.serviceInterface.carryOutTransaction(enterpriseId, clientId, value, transactionType);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
