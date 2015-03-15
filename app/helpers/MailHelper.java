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
		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"+message);

		MailerPlugin.send(mail);
		
	}
	
public static void sendEmailVerification(String email, String message) {
		
		Email mail = new Email();
		mail.setSubject("Potvrda emaila");
		mail.setFrom("bitpikgroup@gmail.com");
		mail.addTo("bitpikgroup@gmail.com");
		mail.addTo(email);		
		mail.setBodyText(message);
		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"+message);

		MailerPlugin.send(mail);
		
	}
	
}