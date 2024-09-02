package dev.joaobertholino.tgidtechnicaltest.service.mailutil;

import dev.joaobertholino.tgidtechnicaltest.model.Client;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendMailUtil {
	private static JavaMailSender mailSender;

	public SendMailUtil(JavaMailSender mailSender) {
		SendMailUtil.mailSender = mailSender;
	}

	public static void sandNotificationClient(Client client, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(client.getEmail());
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		SendMailUtil.mailSender.send(mailMessage);
	}
}
