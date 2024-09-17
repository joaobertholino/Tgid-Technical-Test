package dev.joaobertholino.tgidtechnicaltest.service;

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
public class TransactionService {
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

	@Transactional
	public void carryOutTransaction(Integer enterpriseId, Integer clientId, BigDecimal value, TransactionType transactionType) {
		Enterprise enterprise = this.enterpriseRepository.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFound("Enterprise not found in database"));
		Client client = this.clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFound("Client not found in database"));

		Double totalTaxPercent = calculateTaxPercent(enterprise, transactionType);
		BigDecimal totalDiscount = value.multiply(BigDecimal.valueOf(totalTaxPercent));

		BigDecimal finalValue = validateFinalValue(enterprise, transactionType, value, totalDiscount);
		enterprise.setBalance(finalValue);
		this.enterpriseRepository.save(enterprise);

		Transaction transaction = new Transaction(enterprise, client, transactionType, value.add(totalDiscount));
		this.transactionRepository.save(transaction);

		SendMailUtil.sandNotificationClient(client, "Transaction carried out", "Your transaction worth " + transaction.getValue() + " was successful.");
		this.restTemplate.postForEntity(this.webhookUrl, transaction, String.class);
	}

	private BigDecimal validateFinalValue(Enterprise enterprise, TransactionType transactionType, BigDecimal value, BigDecimal totalDiscount) {
		if (transactionType.equals(TransactionType.WITHDRAWAL) && (enterprise.getBalance().doubleValue() > 0.0 && enterprise.getBalance().doubleValue() > value.doubleValue())) {
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
				listTaxPercent.add(tax.getPercent());
			}
		});
		return listTaxPercent.stream().filter(Objects::nonNull).reduce(0.0, Double::sum);
	}
}
