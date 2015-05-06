package controllers;

import helpers.JsonHelper;
import helpers.MailHelper;
import helpers.SessionHelper;

import java.util.List;

import models.Blogger;
import models.MainCategory;
import models.Product;
import models.User;
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
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * Application Class; Contains our main methods and one inner class Contact;
 * Methods contained in the class are : - index(); - newViewForFilter(); -
 * chooseRegistration(); - registrationPikStore(); - registration(); - login();
 * - logout(); - contact(); - sendMail();
 */
public class Application extends Controller {

	/**
	 * Inner private class used for the Contact function; The function to send
	 * the message to the bitpikgroup@gmail.com which is the admins and the
	 * bitpik portal official email;
	 *
	 */
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
		 * Constructor with parameters;
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
	 * Method index() renders the index.html page with the following parameters,
	 * productsList - which are all the products which are not sold and not
	 * special (not distinguished products)
	 * specialProductList - all the products that are not sold and are made special
	 * (are distinguished products);
	 * mainCategoryList - the list of our categories;
	 * pikShops - list of all of our registered pikShops;
	 * @return Result rendering the index.html
	 */
	public static Result index() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		List<Product> productList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", false).findList();
		List<Product> specialProductList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", true).findList();

		List<MainCategory> mainCategoryList = MainCategory.find.all();
		List<User> pikShops = User.find.where().eq("isPikStore", true).findList();
		if (!request().accepts("text/html")) {
			return JsonController.indexAndroid(productList);
		}
		return ok(index.render(specialProductList, productList, mainCategoryList, pikShops));
	}

	/**
	 * Method newViewForFiler() renders the newViewForFilter.html page with the
	 * folowing parameters, productsList - which are all the products which are
	 * not sold and not special (not distinguished products) specialProductList
	 * - all the products that are not sold and are made special (are
	 * distinguished products); mainCategoryList - the list of our categories;
	 * 
	 * @return Result rendering the newViewForFilter.html
	 */
	public static Result newViewForFilter(){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		List<Product> productList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", false).findList();
		List<Product> specialProductList = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", true).findList();
		return ok(newViewForFilter.render(specialProductList,productList,MainCategory.allMainCategories()));
	}
	
	/**
	 * Method chooseRegistration() renders the chooseRegistration.html page;
	 * Where the user will choose to register as a regular User or a pikShop;
	 * 
	 * @return Result rendering the chooseRegistration.html
	 */
	public static Result chooseRegistration(){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		return ok(chooseRegistration.render("",""));
	}
	
	/**
	 * Method registrationPikStore() renders the registrationPikStore.html page;
	 * Where we give the user the forms to enter and fill to register as a
	 * pikShop; MainCategory.allMainCategories() - is the list that is rendered
	 * on the page where the one who is about to register as a pikShop will
	 * choose which of the categories will his pikShop do business in;
	 * 
	 * @return Result rendering the chooseRegistration.html
	 */
	public static Result registrationPikStore(){

		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		return ok(registrationPikStore.render("","",MainCategory.allMainCategories()));
	}
	
	/**
	 * Method registration() renders the registration.html page;
	 * 
	 * @return Result rendering the registration.html
	 */
	public static Result registration() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		if (!request().accepts("text/html")) {
			return JsonController.registration();
		}
		return ok(registration.render("", ""));
	}

	/**
	 * Method login() renders the login.html page; Where we give the User the
	 * proper forms to fill in in order for the User to the web page;
	 * 
	 * @return Result rendering the login.html
	 */
	public static Result login() {
		
		if (!request().accepts("text/html")) {
			return JsonController.login();
		}
		return ok(login.render("", ""));

	}

	/**
	 * Method Logout() is the method that clears the current session and
	 * redirects to index.html
	 * 
	 * @return Redirecting the user to index.html
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
	* @return Result rendering the contact.html
	*/
	public static Result contact() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		return ok(contact.render(new Form<Contact>(Contact.class)));
	}

	/**
	 * The "recaptcha metod() www.google.com";
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
								flash("fail", Messages.get("Molim Vas popunite dio za poruku."));
								return redirect("/contact");
							}
							flash("success", Messages.get("Poruka je poslata."));
							MailHelper.sendContactMessage(email, message);
							Logger.of("user").info("Sending email with ContactForm successfull ["+ email +"]");
							return redirect("/contact");
						} else {
							flash("error", "Desila se greška pri slanju poruke.");
							Logger.of("user").info("Error sending email with ContactForm");
							return ok(contact.render(submit));
						}
					}
				});
		// return the promise
		return holder;
	}
}
