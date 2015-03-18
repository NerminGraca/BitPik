package controllers;

import java.util.List;

import models.MainCategory;
import models.Product;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.Logger;


public class Application extends Controller {
	
	/**
	 * Method index renders the index.hmtl page
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
		Logger.of("login").info("User registered");
		return ok(registration.render( "", ""));
	}

	/**
	 * Renders the login.html page;
	 * 
	 * @return
	 */
	public static Result login() {
		Logger.of("login").info("User logged");
		return ok(login.render("", ""));
		
	}
	
	/**
	 * @author Sanela Grcic & Nermin Graca 
	 * Method Logout - clears current session and redirects to index.html
	 * @return redirect to index.html
	 */
	public static Result logout() {
		Logger.of("login").info("User logout");
		session().clear();
		flash("logout", Messages.get("Odjavili ste se. "));
		return redirect(routes.Application.index());
	}

}
