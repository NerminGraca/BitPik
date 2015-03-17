package helpers;

import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailHelper {

	public static void send(String email, String message) {

		Email mail = new Email();
		mail.setSubject("Potvrda registracije");
		mail.setFrom("bitpikgroup@gmail.com");
		mail.addTo("bitpikgroup@gmail.com");
		mail.addTo(email);
		mail.setBodyText(message);
		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
				+ message);

		MailerPlugin.send(mail);

	}

	public static void sendEmailVerification(String email, String message) {

		Email mail = new Email();
		mail.setSubject("Potvrda emaila");
		mail.setFrom("bitpikgroup@gmail.com");
		mail.addTo("bitpikgroup@gmail.com");
		mail.addTo(email);
		mail.setBodyText(message);
		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
				+ message);

		MailerPlugin.send(mail);

	}
	
	/**
	 * first creates the Email object, then sets its properties(some of them with values passed as parameters),
	 * and finally, sends that object
	 * @param email email
	 * @param message message
	public static void sendContactMessage(String email, String message) {

		Email mail = new Email();
		mail.setSubject("Contact request BitPik");
		mail.setFrom("BitPik Contact <bitpikgroup@gmail.com>");
		mail.addTo("BitPik Admin <nermin.vucinic@gmail.com>");
		mail.addTo(email);

		mail.setBodyText(message);
		mail.setBodyHtml(String
				.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>",
						email, message));
		MailerPlugin.send(mail);
	}

}
