package controllers;

import helpers.MailHelper;

import java.util.ArrayList;
import java.util.List;

import models.MainCategory;
import models.Product;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;


public class Application extends Controller {
	
	/**
	 * Either directs to the index.html with the session name already logged in
	 * or directs to the index.html page with "Unknown" as username;
	 * 
	 * @return
	 */
	public static Result index() {
		List<Product> productList = ProductController.findProduct.all();
		List<MainCategory> mainCategoryList = MainCategory.find.all();
 
		return ok(index.render(productList, mainCategoryList));		
	}
	
	/**
	 * Renders the registration.html page;
	 * 
	 * @return
	 */
	public static Result registration() {
		return ok(registration.render( "", ""));
	}

	/**
	 * Renders the login.html page;
	 * 
	 * @return
	 */
	public static Result login() {
		return ok(login.render("", ""));
		
	}
	
	/**
	 * @author Sanela Grcic & Nermin Graca 
	 * Method Logout - clears current session and redirects to index.html
	 * @return redirect to index.html
	 */
	public static Result logout() {
		session().clear();
		flash("logout", Messages.get("Odjavili ste se. "));
		return redirect(routes.Application.index());
	}

}
