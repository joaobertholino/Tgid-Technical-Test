package dev.joaobertholino.tgidtechnicaltest.service.mailutil;

import dev.joaobertholino.tgidtechnicaltest.model.Client;
import dev.joaobertholino.tgidtechnicaltest.model.enums.TransactionType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class SendMailUtil {
	private static JavaMailSender mailSender;

	public SendMailUtil(JavaMailSender mailSender) {
		SendMailUtil.mailSender = mailSender;
	}

	public static void sandNotificationClient(Client client, BigDecimal value, TransactionType transactionType) {
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

		StringBuilder text = new StringBuilder()
				.append(transactionType.getTransactionTypeName())
				.append(" worth ")
				.append(currencyFormatter.format(value))
				.append(" was made successfully.");

		StringBuilder subject = new StringBuilder()
				.append(transactionType.getTransactionTypeName())
				.append(" made successfully");

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(client.getEmail());
		mailMessage.setSubject(subject.toString());
		mailMessage.setText(text.toString());
		SendMailUtil.mailSender.send(mailMessage);
	}
}
