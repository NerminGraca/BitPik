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
		mail.setBodyHtml("Kopirajte ovaj link u vas URL da biste verifikovali vas email :\n"+message);

		MailerPlugin.send(mail);
		
	}
}