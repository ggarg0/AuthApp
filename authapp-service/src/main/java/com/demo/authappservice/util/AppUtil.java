package com.demo.authappservice.util;

import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.springframework.http.HttpHeaders;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class AppUtil {
	public static final String ADMIN_MAILID = "xyz@gmail.com";

	public static String getLoggedUserFromHeader(HttpHeaders header) {
		String loggedUser = "";
		if (header != null && header.get("Username") != null)
			loggedUser = getStringFromList(header.get("Username"), ", ");

		return loggedUser;
	}

	public static String getTokenFromHeader(HttpHeaders header) {
		String token = "";
		if (header != null && header.get("Authorization") != null) {
			String auth = header.get("Authorization").get(0);
			token = (auth == null || auth.contains("null") ? null : auth.split(" ", 2)[1]);
		}
		return token;
	}

	public static String getStringFromList(List<String> list, String delimiter) {
		String result = "";
		StringBuilder output = new StringBuilder();
		if (!list.isEmpty()) {
			for (String issue : list)
				output.append(issue).append(delimiter);

			result = output.substring(0, output.length() - 2);
		}
		return result;
	}

	public static void sendMail(String toMailId, String ccMailId, String mailSubject, String mailBody,
			boolean sendToAdmin, boolean isHTMLContent) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		Session session = null;

		try {
			session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(ADMIN_MAILID, "");
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(ADMIN_MAILID));
			if (!toMailId.trim().isEmpty()) {
				if (toMailId.contains(";")) {
					StringTokenizer toRecipient = new StringTokenizer(toMailId, ";");
					while (toRecipient.hasMoreElements())
						message.addRecipient(Message.RecipientType.TO,
								new InternetAddress(toRecipient.nextElement().toString().trim()));
				} else {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMailId));
				}
			}
			if (ccMailId != null && !ccMailId.isEmpty())
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccMailId));

			if (sendToAdmin)
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(ADMIN_MAILID));

			message.setSubject(mailSubject);

			if (isHTMLContent)
				message.setContent(mailBody, "text/html");
			else
				message.setText(mailBody);

			Transport.send(message);
		} catch (Exception e) {
			System.out.println("Error while sending mail");
		} finally {
			session = null;
		}
	}

}
