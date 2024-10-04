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
	void carryOutTransaction() {
		Enterprise enterpriseMock = new Enterprise(BigDecimal.valueOf(100.00), "87600058000154", 1, "Enterprise Test");
		Client clientMock = new Client("45301651838", "noreply@email.com", 1, "Client Test");

		Tax taxOne = new Tax(TransactionType.WITHDRAWAL, enterpriseMock, BigDecimal.valueOf(0.2));
		Tax taxTwo = new Tax(TransactionType.DEPOSIT, enterpriseMock, BigDecimal.valueOf(0.1));
		enterpriseMock.getTaxList().addAll(Arrays.asList(taxOne, taxTwo));

		Mockito.when(this.enterpriseRepository.findById(enterpriseMock.getId())).thenReturn(Optional.of(enterpriseMock));
		Mockito.when(this.clientRepository.findById(clientMock.getId())).thenReturn(Optional.of(clientMock));

		Optional<Enterprise> optionalEnterprise = this.enterpriseRepository.findById(1);
		Optional<Client> optionalClient = this.clientRepository.findById(1);

		Assertions.assertDoesNotThrow(() -> optionalEnterprise.orElseThrow());
		Assertions.assertInstanceOf(Enterprise.class, optionalEnterprise.get());

		Assertions.assertDoesNotThrow(() -> optionalClient.orElseThrow());
		Assertions.assertInstanceOf(Client.class, optionalClient.get());
	}
}
