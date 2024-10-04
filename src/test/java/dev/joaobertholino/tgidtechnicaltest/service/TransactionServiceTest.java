package dev.joaobertholino.tgidtechnicaltest.service;

import dev.joaobertholino.tgidtechnicaltest.model.Client;
import dev.joaobertholino.tgidtechnicaltest.model.Enterprise;
import dev.joaobertholino.tgidtechnicaltest.model.Tax;
import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import dev.joaobertholino.tgidtechnicaltest.repository.ClientRepository;
import dev.joaobertholino.tgidtechnicaltest.repository.EnterpriseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	@Mock
	private ClientRepository clientRepository;

	@Mock
	private EnterpriseRepository enterpriseRepository;

	@Test
	@DisplayName(value = "Test that verifies the recovery of Enterprise and Client.")
	void validateTrueData() {
		Enterprise enterpriseTest = new Enterprise(BigDecimal.valueOf(100), "87600058000154", 1, "Enterprise Test One");
		Tax taxOne = new Tax(TransactionType.WITHDRAWAL, enterpriseTest, BigDecimal.valueOf(0.2));
		Tax taxTwo = new Tax(TransactionType.DEPOSIT, enterpriseTest, BigDecimal.valueOf(0.1));
		enterpriseTest.getTaxList().addAll(Arrays.asList(taxOne, taxTwo));

		Client clientOne = new Client("45301651838", "noreply@email.com", 1, "Client Test One");

		Mockito.when(this.enterpriseRepository.findById(enterpriseTest.getId())).thenReturn(Optional.of(enterpriseTest));
		Mockito.when(this.clientRepository.findById(clientOne.getId())).thenReturn(Optional.of(clientOne));

		Optional<Enterprise> optionalEnterprise = this.enterpriseRepository.findById(1);
		Optional<Client> optionalClient = this.clientRepository.findById(1);

		Assertions.assertDoesNotThrow(() -> optionalEnterprise.orElseThrow());
		Assertions.assertEquals(BigDecimal.valueOf(100), optionalEnterprise.get().getBalance());
		Assertions.assertInstanceOf(Enterprise.class, optionalEnterprise.get());

		Assertions.assertDoesNotThrow(() -> optionalClient.orElseThrow());
		Assertions.assertInstanceOf(Client.class, optionalClient.get());
	}
}
