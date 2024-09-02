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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {
	private final ClientRepository clientRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final TransactionRepository transactionRepository;

	@Value(value = "${webhook.url}")
	private String webhookUrl;

	public TransactionService(ClientRepository clientRepository, EnterpriseRepository enterpriseRepository, TransactionRepository transactionRepository) {
		this.clientRepository = clientRepository;
		this.enterpriseRepository = enterpriseRepository;
		this.transactionRepository = transactionRepository;
	}

	@Transactional
	public void carryOutTransaction(Integer enterpriseId, Integer clientId, BigDecimal value, TransactionType transactionType) {
		Enterprise enterprise = this.enterpriseRepository.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFound("Enterprise not found in database"));
		Client client = this.clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFound("Client not found in database"));

		Double totalTaxPercent = calculateTaxPercent(enterprise, transactionType);
		BigDecimal totalDiscount = value.multiply(BigDecimal.valueOf(totalTaxPercent));

		BigDecimal finalValue;
		if (transactionType.equals(TransactionType.WITHDRAWAL) && (enterprise.getBalance().doubleValue() > 0.0 && enterprise.getBalance().doubleValue() > value.doubleValue())) {
			finalValue = enterprise.getBalance().subtract(value.add(totalDiscount));
		} else if (transactionType.equals(TransactionType.DEPOSIT) && value.doubleValue() > 0.0) {
			finalValue = enterprise.getBalance().add(value.subtract(totalDiscount));
		} else {
			throw new TransactionValueInvalid("Invalid transaction values");
		}

		enterprise.setBalance(finalValue);
		this.enterpriseRepository.save(enterprise);

		Transaction transaction = new Transaction(enterprise, client, transactionType, totalDiscount);
		this.transactionRepository.save(transaction);

		DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
		SendMailUtil.sandNotificationClient(client, "Transaction carried out", "Your transaction worth " + decimalFormat.format(transaction.getValue()) + " was successful.");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.postForEntity(this.webhookUrl, transaction, String.class);
	}

	private Double calculateTaxPercent(Enterprise enterprise, TransactionType transactionType){
		List<Double> listTaxPercent = new ArrayList<>();
		enterprise.getTaxList().forEach(tax -> {
			if (tax.getTransactionType().equals(transactionType)) {
				listTaxPercent.add(tax.getPercent());
			}
		});
		return listTaxPercent.stream().filter(Objects::nonNull).reduce(0.0, Double::sum);
	}
}
