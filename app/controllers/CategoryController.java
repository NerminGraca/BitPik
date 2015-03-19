package controllers;

import helpers.AdminFilter;

import java.util.ArrayList;
import java.util.List;

import models.*;
import play.Logger;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import views.html.*;

public class CategoryController extends Controller {
	
	//Finders
	static Form<MainCategory> newMainCategory = new Form<MainCategory>(MainCategory.class);
	static Finder<Integer, MainCategory> findMainCategory = new Finder<Integer, MainCategory>(Integer.class, MainCategory.class);
	
	/**
	 * @author Graca Nermin
	 * Method categories finds all entries in database in table Main_Category and collects them and sends
	 * them to view which will represent them correctly
	 * @param id
	 * @return
	 */
	public static Result categories(int id) {
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		MainCategory mc = MainCategory.findMainCategory(id);
		List<Product> productList = ProductController.findProduct.where().eq("mainCategory", mc).findList();
		return ok(kategorija.render(productList, mainCategoryList));
	}
	
	/**
	 * @author Graca Nermin
	 * Method allCategory list all of present categories at single view
	 * @return
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result allCategory() {		
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(listaKategorija.render(mainCategoryList));
	}
	
	/**
	 * @author Graca Nermin
	 * Method editMainCategory allows administrator to edit one of the category
	 * @param id = id of category which will be edited
	 * @return = New view in which edit is performed
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result editMainCategory(int id) {
		Logger.of("category").info("Updated category");
		MainCategory mc = findMainCategory.byId(id);
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		ArrayList<String> adminList = new ArrayList<String>();

		return ok(editMainCategory.render(mc, mainCategoryList, adminList ));
	}
	
	/**
	 * Method saveEditMainCategory allows administrator to save changes made to category
	 * in to the database
	 * @param id = id of category which was edited
	 * @return to view of all categories
	 */
	public static Result saveEditMainCategory(int id) {
		//takes the new attributes that are entered in the form;
		String name = newMainCategory.bindFromRequest().get().name;
		
		// sets all the new entered attributes as the original ones from the product;
		// and saves();
		MainCategory mc = findMainCategory.byId(id);
		mc.setName(name);
		mc.save();
		
		return redirect(routes.CategoryController.allCategory());		
	}
	
	/**
	 * Method deleteMainCategory deletes gives category from the views and the database
	 * @param id = id of category which was deleted
	 * @return
	 */
	public static Result deleteMainCategory(int id) {
		  Logger.of("category").info("Deleted category");
		  MainCategory.delete(id);
		  return redirect(routes.CategoryController.allCategory());
	}
	
	/**
	 * @author Graca Nermin
	 * Method addMainCategory adds new category into the list and also in the database and
	 * it will also check if that name is taken
	 * @return to the view of all categories with new list shown
	 */
	public static Result addMainCategory() {
		Logger.of("category").info("Added main category");
		String name = newMainCategory.bindFromRequest().get().name;
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		if (MainCategory.allMainCategories().contains(MainCategory.findMainCategoryByName(name))) {
			return redirect(routes.CategoryController.allCategory());
		} else {
			MainCategory.createMainCategory(name);
			return redirect(routes.CategoryController.allCategory());
		}			
	}
	
}
