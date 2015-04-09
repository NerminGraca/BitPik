package controllers;

import helpers.MailHelper;

import java.util.List;

import models.MainCategory;
import models.Product;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import com.fasterxml.jackson.databind.JsonNode;

public class Application extends Controller {

	public static class Contact {
		
		@Required
		@Email
		public String email;
		
		@Required
		public String message;
		
		/**
		 * Default constructor;
		 */
		public Contact() {
			super();
			this.email = "Unknown";
			this.message = "Unknown";
		}
		/**
		 * Constructor
		 * @param email
		 * @param message
		 */
		public Contact(String email, String message) {
			super();
			this.email = email;
			this.message = message;
		}
				
	}

	/**
	 * Method index renders the index.html page
	 * 
	 * @return
	 */
	public static Result index() {
		List<Product> productList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", false).findList();
		List<Product> specialProductList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", true).findList();
		List<MainCategory> mainCategoryList = MainCategory.find.all();

		return ok(index.render(specialProductList, productList, mainCategoryList));
	}

	public static Result newViewForFilter(){
		return ok(newViewForFilter.render(Product.find.all(),MainCategory.allMainCategories()));
	}
	/**
	 * Method registration renders the registration.html page;
	 * 
	 * @return
	 */
	public static Result registration() {
		return ok(registration.render("", ""));
	}

	/**
	 * Method login renders the login.html page;
	 * 
	 * @return
	 */
	public static Result login() {
		return ok(login.render("", ""));

	}

	/**
	 * Method Logout - clears current session and redirects to index.html
	 * @return redirect to index.html
	 */
	public static Result logout() {
		Logger.info("User "+ session("username") +" loged out");
		session().clear();
		flash("logout", Messages.get("Odjavili ste se."));
		return redirect(routes.Application.index());
	}

	/**
	* Method contact renders the contact.html page;
	* 
	* @return
	*/
	public static Result contact() {
		return ok(contact.render(new Form<Contact>(Contact.class)));
	}

	/**
	 * We return whatever the promise returns, so the return value is changed
	 * from Result to Promise<Result>
	 *
	 * @return the contact page with a message indicating if the email has been
	 *         sent.
	 */
	public static Promise<Result> sendMail() {
		// need this to get the google recapctha value
		final DynamicForm temp = DynamicForm.form().bindFromRequest();
		/*
		 * send a request to google recaptcha api with the value of our secret
		 * code and the value of the recaptcha submitted by the form
		 */
		Promise<Result> holder = WS
				.url("https://www.google.com/recaptcha/api/siteverify")
				.setContentType("application/x-www-form-urlencoded")
				.post(String.format(
						"secret=%s&response=%s",
						// get the API key from the config file
						Play.application().configuration()
								.getString("recaptchaKey"),
						temp.get("g-recaptcha-response")))
				.map(new Function<WSResponse, Result>() {
					// once we get the response this method is loaded
					public Result apply(WSResponse response) {
						// get the response as JSON
						JsonNode json = response.asJson();
						Form<Contact> submit = Form.form(Contact.class)
								.bindFromRequest();
						// check if value of success is true
						if (json.findValue("success").asBoolean() == true
								&& !submit.hasErrors()) {
							Contact newMessage = submit.get();
							String email = newMessage.email;
							String message = newMessage.message;
							// BE-Security -if the message is not entered; 
							if (message.equals("")) {
								flash("fail", Messages.get("Molim vas popunite dio za poruku"));
								return redirect("/contact");
							}
							flash("success", Messages.get("Poruka je poslana"));
							MailHelper.sendContactMessage(email, message);
							Logger.of("user").info("Sending email with ContactForm successfull ["+ email +"]");
							return redirect("/contact");
						} else {
							flash("error", "Desila se greska pri slanju poruke");
							Logger.of("user").info("Error sending email with ContactForm");
							return ok(contact.render(submit));
						}
					}
				});
		// return the promise
		return holder;
	}
}
