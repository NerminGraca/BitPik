package controllers;

import java.util.List;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class CategoryController extends Controller {
	
	static String usernameSes;
	
	/**
	 * Method categories finds all entries in database in table Main_Category and collects them and sends
	 * them to view which will represent them correctly
	 * @param id
	 * @return
	 */
	public static Result categories(int id) {
		
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		MainCategory mc = MainCategory.findMainCategory(id);
		List<Product> productList = ProductController.findProduct.where().eq("category", mc.name).findList();
		return ok(kategorija.render(usernameSes, productList, mainCategoryList));
	}

}
