package controllers;

import java.util.ArrayList;
import java.util.List;

import models.MainCategory;
import models.Product;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {
	
	static ArrayList<String> adminList = new ArrayList<String>();
	static String usernameSes;
	
	/**
	 * Renders the registration.html page;
	 * 
	 * @return
	 */
	public static Result registration() {
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		} 
		return ok(registration.render(usernameSes, "", ""));
	}

	/**
	 * Renders the login.html page;
	 * 
	 * @return
	 */
	public static Result login() {
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		return ok(login.render(usernameSes, "", ""));
		
	}
	
	/**
	 * @author Sanela Grcic & Nermin Graca Method Logout - clears current
	 *         session and redirects to index.html
	 * @return redirect to index.html
	 */
	public static Result logout() {
		session().clear();
		return redirect("/");
	}

}
