package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import models.Product;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import play.Logger;
import play.Play;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailHelper {

	/**
	 * Method send sends email to the Users email which has just registered
	 * @param email
	 * @param message
	 */
	public static void send(String email, String message) {

//		Email mail = new Email();
//		mail.setSubject("Potvrda registracije");
//		mail.setFrom("bitpikgroup@gmail.com");
//		mail.addTo("bitpikgroup@gmail.com");
//		mail.addTo(email);
//		mail.setBodyText(message);
//		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
//				+ message);
//
//		MailerPlugin.send(mail);
		
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Potvrda registracije");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");
			mail.addTo(email);
			mail.setMsg(message);
			mail.setHtmlMsg("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
					+ message);
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));

			mail.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method sendEmailVerification sends email to User which changed his already verified
	 * email in order to verify this new email
	 * @param email
	 * @param message
	 */
	public static void sendEmailVerification(String email, String message) {

//		Email mail = new Email();
//		mail.setSubject("Potvrda emaila");
//		mail.setFrom("bitpikgroup@gmail.com");
//		mail.addTo("bitpikgroup@gmail.com");
//		mail.addTo(email);
//		mail.setBodyText(message);
//		mail.setBodyHtml("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
//				+ message);
//
//		MailerPlugin.send(mail);
		
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Potvrda emaila");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");
			mail.addTo(email);
			mail.setMsg(message);
			mail.setHtmlMsg("Kliknite na slijedeci link kako biste verifikovali vas email :\n"
					+ message);
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));

			mail.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Method sendRefundEmail sends email to users when product has been sent to refund
	 * @param buyerEmail
	 * @param sellerEmail
	 * @param message
	 */
	public static void sendRefundEmail(String buyerEmail, String sellerEmail, String message) {

//		Email mail = new Email();
//		mail.setSubject("Refundacija aktivirana");
//		mail.setFrom("bitpikgroup@gmail.com");
//		mail.addTo("bitpikgroup@gmail.com");
//
//		mail.addTo("nera.graca@gmail.com");
//
//		mail.addTo(buyerEmail);
//		mail.addTo(sellerEmail);
//		mail.setBodyText(message);
//		mail.setBodyHtml("Artikal je poslan na refundaciju :\n"
//				+ message);
//
//		MailerPlugin.send(mail);

		
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Refundacija aktivirana");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");
			mail.addTo(buyerEmail);
			mail.addTo(sellerEmail);
			mail.setMsg(message);
			mail.setHtmlMsg("Artikal je poslan na refundaciju :\n"
					+ message);
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));

			mail.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Method sendRefundEmailDenial sends email to users when product refund has been denied
	 * @param buyerEmail
	 * @param sellerEmail
	 * @param message
	 */
	public static void sendRefundEmailDenial(String buyerEmail, String sellerEmail, String message) {

//		Email mail = new Email();
//		mail.setSubject("Refundacija otkazana");
//		mail.setFrom("bitpikgroup@gmail.com");
//		mail.addTo("bitpikgroup@gmail.com");
//
//		mail.addTo("nera.graca@gmail.com");
//
//		mail.addTo(buyerEmail);
//		mail.addTo(sellerEmail);
//		mail.setBodyText(message);
//		mail.setBodyHtml("Refundacija artikla nije odobrena :\n"
//				+ message);
//
//		MailerPlugin.send(mail);
		
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Refundacija otkazana");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");
			mail.addTo(buyerEmail);
			mail.addTo(sellerEmail);
			mail.setMsg(message);
			mail.setHtmlMsg("Refundacija artikla nije odobrena :\n"
					+ message);
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));

			mail.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	/**
	 * first creates the Email object, then sets its properties(some of them with values passed as parameters),
	 * and finally, sends that object
	 * @param email email
	 * @param message message
	 */
	public static void sendContactMessage(String email, String message) {
		if (message != null) {
//		Email mail = new Email();
//		mail.setSubject("Contact request BitPik");
//		mail.setFrom("BitPik Contact <bitpikgroup@gmail.com>");
//		mail.addTo("bitpikgroup@gmail.com");
//		mail.addTo(email);
//		mail.setBodyText(message);
//		mail.setBodyHtml(String
//				.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>",
//						email, message));
//		MailerPlugin.send(mail);
			
			try {
				HtmlEmail mail = new HtmlEmail();
				mail.setSubject("Contact request BitPik");
				mail.setFrom("bitpikgroup@gmail.com");
				mail.addTo("bitpikgroup@gmail.com");
				mail.addTo(email);
				mail.setMsg(message);
				mail.setHtmlMsg(String
					.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>",
								email, message));
				mail.setHostName("smtp.gmail.com");
				mail.setStartTLSEnabled(true);
				mail.setSSLOnConnect(true);
				mail.setAuthenticator(new DefaultAuthenticator(
						Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
						Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
						));

				mail.send();
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void sendNewsletterMessage(String email, String message){
//		Email mail = new Email();
//		mail.setSubject("Novosti sa BitPik-a");
//		mail.setFrom("bitpikgroup@gmail.com");
//		mail.addTo("bitpikgroup@gmail.com");
//
//		mail.addTo(email);
//		mail.setBodyText(message);
//		mail.setBodyHtml(message);
//		MailerPlugin.send(mail);
		
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Novosti sa BitPik-a");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");
			mail.addTo(email);
			mail.setMsg(message);
			mail.setHtmlMsg(message);
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));

			mail.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void sendNewsletter(String email, String message, List<Product> products) {
		Scanner input = null;
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Novosti sa BitPik-a");
			mail.setFrom("bitpikgroup@gmail.com");
			mail.addTo("bitpikgroup@gmail.com");

			mail.addTo(email);
			mail.setMsg(message);
			mail.setHtmlMsg(message);

			

			input = new Scanner(new File("./public/templates/newsletter.html"));
			while (input.hasNextLine()) {
				message += input.nextLine();
			}

			Document doc = getHTML(email, message, products);
			doc.getElementById("appendableText").append("newsletter");
			mail.setHtmlMsg(doc.toString());

			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(Play.application()
					.configuration().getString("EMAIL_USERNAME_ENV"), Play
					.application().configuration()
					.getString("EMAIL_PASSWORD_ENV")));
			mail.send();

		} catch (Exception e) {
			Logger.error("Error");
		} finally {
			input.close();
		}
	}
	
	private static Document getHTML(String email,String html, List<Product> products){
		
		Document doc = Jsoup.parse(html);

		
		
		return doc;
	}
	
	
	
	
}
