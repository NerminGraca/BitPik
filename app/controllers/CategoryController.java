package controllers;

import java.util.ArrayList;
import java.util.List;

import models.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
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

	
	

	static Form<MainCategory> newMainCategory = new Form<MainCategory>(MainCategory.class);

	static Finder<Integer, MainCategory> findMainCategory = new Finder<Integer, MainCategory>(Integer.class, MainCategory.class);

	
	public static Result editMainCategory(int id) {
		usernameSes = session("username");
		MainCategory mc = findMainCategory.byId(id);
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		ArrayList<String> adminList = new ArrayList<String>();

		return ok(editMainCategory.render(usernameSes, mc, mainCategoryList, adminList ));
	}
	
	public static Result saveEditMainCategory(int id) {
		//takes the new attributes that are entered in the form;
		usernameSes = session("username");
		String name = newMainCategory.bindFromRequest().get().name;
		
		
		// sets all the new entered attributes as the original ones from the product;
		// and saves();
		MainCategory mc = findMainCategory.byId(id);
		mc.setName(name);
		mc.save();
		
		return redirect("/listaKategorija");	
		
	}
	
	
	
	
	public static Result deleteMainCategory(int id) {
		  MainCategory.delete(id);
		  return redirect(routes.CategoryController.allCategory());
	}	
	
	public static Result allCategory() {
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		} 
		ArrayList<String> adminList = new ArrayList<String>();
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(listaKategorija.render(usernameSes, adminList, mainCategoryList));
	}
	
	
	
	
}
