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
		Logger.of("login").info("User "+ session("username") +" loged out");
		session().clear();
		flash("logout", Messages.get("Odjavili ste se. "));
		return redirect(routes.Application.index());
	}

}
