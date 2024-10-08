package dev.joaobertholino.tgidtechnicaltest.service;

import dev.joaobertholino.tgidtechnicaltest.controller.requesttemplate.Request;
import dev.joaobertholino.tgidtechnicaltest.model.Client;
import dev.joaobertholino.tgidtechnicaltest.model.Enterprise;
import dev.joaobertholino.tgidtechnicaltest.model.Transaction;
import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import dev.joaobertholino.tgidtechnicaltest.repository.ClientRepository;
import dev.joaobertholino.tgidtechnicaltest.repository.EnterpriseRepository;
import dev.joaobertholino.tgidtechnicaltest.repository.TransactionRepository;
import dev.joaobertholino.tgidtechnicaltest.service.exception.ClientNotFound;
import dev.joaobertholino.tgidtechnicaltest.service.exception.EnterpriseNotFound;
import dev.joaobertholino.tgidtechnicaltest.service.exception.TransactionValueInvalid;
import dev.joaobertholino.tgidtechnicaltest.service.mailutil.SendMailUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService implements ServiceInterface {
	private final ClientRepository clientRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final TransactionRepository transactionRepository;
	private final RestTemplate restTemplate;

	@Value(value = "${webhook.url}")
	private String webhookUrl;

	public TransactionService(ClientRepository clientRepository, EnterpriseRepository enterpriseRepository, TransactionRepository transactionRepository, RestTemplate restTemplate) {
		this.clientRepository = clientRepository;
		this.enterpriseRepository = enterpriseRepository;
		this.transactionRepository = transactionRepository;
		this.restTemplate = restTemplate;
	}

	@Override
	@Transactional
	public void carryOutTransaction(Request request) {
		Enterprise enterprise = this.enterpriseRepository.findById(request.enterpriseId()).orElseThrow(() -> new EnterpriseNotFound("Enterprise not found in database"));
		Client client = this.clientRepository.findById(request.clientId()).orElseThrow(() -> new ClientNotFound("Client not found in database"));

		Double totalTaxPercent = calculateTaxPercent(enterprise, request.transactionType());
		BigDecimal totalDiscount = request.value().multiply(BigDecimal.valueOf(totalTaxPercent));

		BigDecimal finalValue = calculateFinalValue(enterprise, request.transactionType(), request.value(), totalDiscount);
		enterprise.setBalance(finalValue);
		this.enterpriseRepository.save(enterprise);

		Transaction transaction = createTransaction(enterprise, client, request.transactionType(), request.value(), totalDiscount);
		this.transactionRepository.save(transaction);

		SendMailUtil.sandNotificationClient(client, transaction.getValue(), transaction.getTransactionType());
		this.restTemplate.postForObject(this.webhookUrl, transaction, String.class);
	}

	private Transaction createTransaction(Enterprise enterprise, Client client, TransactionType transactionType, BigDecimal value, BigDecimal totalDiscount) {
		if (transactionType.equals(TransactionType.DEPOSIT)) {
			return new Transaction(enterprise, client, transactionType, value, totalDiscount, value.subtract(totalDiscount));
		}
		return new Transaction(enterprise, client, transactionType, value, totalDiscount, value.add(totalDiscount));
	}

	private BigDecimal calculateFinalValue(Enterprise enterprise, TransactionType transactionType, BigDecimal value, BigDecimal totalDiscount) {
		if (transactionType.equals(TransactionType.WITHDRAWAL) &&
				(enterprise.getBalance().doubleValue() > 0.0 && enterprise.getBalance().doubleValue() >= value.add(totalDiscount).doubleValue())) {
			return enterprise.getBalance().subtract(value.add(totalDiscount));
		} else if (transactionType.equals(TransactionType.DEPOSIT) && value.doubleValue() > 0.0) {
			return enterprise.getBalance().add(value.subtract(totalDiscount));
		} else {
			throw new TransactionValueInvalid("Invalid transaction values");
		}
	}

	private Double calculateTaxPercent(Enterprise enterprise, TransactionType transactionType) {
		List<Double> listTaxPercent = new ArrayList<>();
		enterprise.getTaxList().forEach(tax -> {
			if (tax.getTransactionType().equals(transactionType)) {
				listTaxPercent.add(tax.getPercent().doubleValue());
			}
		});
		return listTaxPercent.stream().filter(Objects::nonNull).reduce(0.0, Double::sum);
	}
}
