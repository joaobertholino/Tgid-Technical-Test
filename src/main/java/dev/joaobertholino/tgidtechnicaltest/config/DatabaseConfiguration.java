package dev.joaobertholino.tgidtechnicaltest.config;

import dev.joaobertholino.tgidtechnicaltest.model.Client;
import dev.joaobertholino.tgidtechnicaltest.model.Enterprise;
import dev.joaobertholino.tgidtechnicaltest.model.Tax;
import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import dev.joaobertholino.tgidtechnicaltest.repository.ClientRepository;
import dev.joaobertholino.tgidtechnicaltest.repository.EnterpriseRepository;
import dev.joaobertholino.tgidtechnicaltest.repository.TaxRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DatabaseConfiguration implements CommandLineRunner {
	private final ClientRepository clientRepository;
	private final EnterpriseRepository enterpriseRepository;
	private final TaxRepository taxRepository;

	public DatabaseConfiguration(ClientRepository clientRepository, EnterpriseRepository enterpriseRepository, TaxRepository taxRepository) {
		this.clientRepository = clientRepository;
		this.enterpriseRepository = enterpriseRepository;
		this.taxRepository = taxRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Client client = new Client("Random Client", "noreply@email.com", "45301651838");
		this.clientRepository.save(client);

		Enterprise enterprise = new Enterprise("Random Enterprise", "87600058000154", BigDecimal.valueOf(100.0));
		this.enterpriseRepository.save(enterprise);

		Tax taxOne = new Tax(TransactionType.WITHDRAWAL, enterprise, 0.2);
		Tax taxTwo = new Tax(TransactionType.DEPOSIT, enterprise, 0.1);
		this.taxRepository.saveAll(Arrays.asList(taxOne, taxTwo));

		enterprise.getTaxList().addAll(Arrays.asList(taxOne, taxTwo));
		this.enterpriseRepository.save(enterprise);
	}
}
