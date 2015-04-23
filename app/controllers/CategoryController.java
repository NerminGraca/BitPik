package controllers;

import helpers.AdminFilter;
import helpers.SessionHelper;

import java.util.List;

import models.*;
import play.Logger;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.i18n.Messages;
import play.mvc.*;
import views.html.*;

public class CategoryController extends Controller {
	
	//Finders
	static Form<MainCategory> newMainCategory = new Form<MainCategory>(MainCategory.class);
	static Form<SubCategory> newSubCategory = new Form<SubCategory>(SubCategory.class);
	static Finder<Integer, MainCategory> findMainCategory = new Finder<Integer, MainCategory>(Integer.class, MainCategory.class);
	
	/**
	 * Method categories finds all entries in database in table Main_Category and collects them and sends
	 * them to view which will represent them correctly
	 * @param id
	 * @return
	 */
	public static Result categories(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		MainCategory mc = MainCategory.findMainCategory(id);
		List<Product> productList = ProductController.findProduct.where().eq("mainCategory", mc).eq("isSold", false).eq("isSpecial", false).findList();
		List<Product> sproductList = ProductController.findProduct.where().eq("mainCategory", mc).eq("isSold", false).eq("isSpecial", true).findList();
		return ok(kategorija.render(sproductList,productList, mainCategoryList, mc));
	}
	
	/**
	 * Method subCategoriesView finds all entries in database which are members of given Sub Category
	 * and sends them to view to be showed
	 * @param id
	 * @return
	 */
	public static Result subCategoriesView(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		SubCategory sc = SubCategory.findSubCategory(id);
		MainCategory mc = sc.mainCategory;
		List<Product> productList = ProductController.findProduct.where().eq("subCategory", sc).eq("isSold", false).eq("isSpecial", false).findList();
		List<Product> sproductList = ProductController.findProduct.where().eq("subCategory", sc).eq("isSold", false).eq("isSpecial", true).findList();
		return ok(podKategorija.render(sproductList,productList, mainCategoryList, mc, sc.name));
	}
	
	/**
	 * Method allCategory list all of present categories at single view
	 * @return
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result allCategory() {	
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(listaKategorija.render(mainCategoryList));
	}
	
	/**
	 * Method subCategories list all Sub Categories of given Main Category
	 * @param id
	 * @return
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result subCategories(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		MainCategory mc = MainCategory.findMainCategory(id);
		return ok(listaPodKategorija.render(mc));
	}
	
	/**
	 * Method editMainCategory allows administrator to edit one of the category
	 * @param id = id of category which will be edited
	 * @return = New view in which edit is performed
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result editMainCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		MainCategory mc = findMainCategory.byId(id);
				
		return ok(editMainCategory.render(mc));
	}
	
	/**
	 * Method editSubCategory allows administrator to edit one of the Sub categories
	 * @param id
	 * @return
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result editSubCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		SubCategory sc = SubCategory.findSubCategory(id);
				
		return ok(editSubCategory.render(sc));
	}
	
	/**
	 * Method saveEditMainCategory allows administrator to save changes made to category
	 * in to the database
	 * @param id = id of category which was edited
	 * @return to view of all categories
	 */
	public static Result saveEditMainCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		//takes the new attributes that are entered in the form;
		String name;
		MainCategory mc = findMainCategory.byId(id);
		try {
			name = newMainCategory.bindFromRequest().get().name;
		} catch(IllegalStateException e) {
			flash("change_maincat_null_field", Messages.get("Molim Vas popunite polje u formi."));
			return ok(editMainCategory.render(mc));
		}
		
		// sets all the new entered attributes as the original ones from the product;
		// and saves();
		
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		if (MainCategory.allMainCategories().contains(MainCategory.findMainCategoryByName(name))) {
			return redirect(routes.CategoryController.editMainCategory(id));
		} else {
			
			String oldname = mc.name;
			mc.setName(name);
			mc.save();
			Logger.of("category").info("Admin updated category " +oldname+" to " + name);
			oldname = null;
			flash("change_maincat_success", Messages.get("Uspješno promijenjen naziv kategorije."));
			return redirect(routes.CategoryController.allCategory());
		}		
	}
	
	/**
	 * Method saveEditSubCategory allows administrator to save changes made to Sub Category
	 * @param id
	 * @return
	 */
	public static Result saveEditSubCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		String name;
		SubCategory sc = SubCategory.findSubCategory(id);
		try {
			name = newSubCategory.bindFromRequest().get().name;
		} catch(IllegalStateException e) {
			flash("change_sub_null_field", Messages.get("Molim Vas popunite polje u formi."));
			return ok(editSubCategory.render(sc));
		}
		
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
				
		MainCategory mc = sc.mainCategory;
		
		if (SubCategory.findSubCategoryByNameAndMainCategory(name, mc)) {
			return redirect(routes.CategoryController.editSubCategory(id));
		} else {
			String oldname = sc.name;
			sc.setName(name);
			sc.save();
			Logger.of("category").info("Admin updated subcategory "+oldname+" to " + sc.name);
			oldname = null;
			flash("change_sub_success", Messages.get("Uspješno promijenjen naziv podkategorije."));
			return redirect(routes.CategoryController.subCategories(mc.id));
		}
	}
	
	/**
	 * Method deleteMainCategory deletes gives category from the views and the database
	 * @param id = id of category which was deleted
	 * @return
	 */
	public static Result deleteMainCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		MainCategory mc = findMainCategory.byId(id);
		if(mc.name.equals("Ostalo")) {
			return redirect(routes.CategoryController.allCategory());
		}
		List<Product> products = mc.products;
		MainCategory various = MainCategory.findMainCategoryByName("Ostalo");
		SubCategory variousSub = various.subCategories.get(0);
		for (Product product : products) {
			product.setCategory(various);
			product.setSubCategory(variousSub);
			product.save();
		}
		MainCategory.delete(id);
		Logger.of("category").info("Admin deleted category " + mc.name);
		return redirect(routes.CategoryController.allCategory());
	}
	
	/**
	 * Method deleteSubCategory deletes gives sub category from the views and the database
	 * @param id
	 * @return
	 */
	public static Result deleteSubCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		SubCategory sc = SubCategory.findSubCategory(id);
		MainCategory mc = sc.mainCategory;
		if(sc.name.equals("Ostalo")) {
			return redirect(routes.CategoryController.subCategories(mc.id));
		}
		SubCategory various = SubCategory.findReturnSubCategoryByNameAndMainCategory("Ostalo", mc);
		List<Product> products = sc.products;
		for (Product product : products) {
			product.setSubCategory(various);
			product.save();
		}
		SubCategory.delete(id);
		Logger.of("category").info("Admin deleted subcategory " + sc.name);
		return redirect(routes.CategoryController.subCategories(mc.id));

	}
	
	/**
	 * Method addMainCategory adds new category into the list and also in the database and
	 * it will also check if that name is taken
	 * @return to the view of all categories with new list shown
	 */
	public static Result addMainCategory() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		String name;
		try {
			name = newMainCategory.bindFromRequest().get().name;
		} catch(IllegalStateException e) {
			flash("add_maincat_null_field", Messages.get("Molim Vas popunite polje u formi."));
			return redirect(routes.CategoryController.allCategory());
		}

		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		if (MainCategory.allMainCategories().contains(MainCategory.findMainCategoryByName(name))) {
			return redirect(routes.CategoryController.allCategory());
		} else {
			MainCategory.createMainCategory(name);
			Logger.of("category").info("Admin added main category " + name);
			flash("add_maincat_success", Messages.get("Uspjesno ste dodali novu kategoriju."));
			return redirect(routes.CategoryController.allCategory());
		}			
	}
	
	/**
	 * Method addSubCategory adds new sub category into the list and also in the database and
	 * it will also check if that name is taken
	 * @param id
	 * @return
	 */
	public static Result addSubCategory(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		MainCategory mc = MainCategory.findMainCategory(id);
		String name;
		try {
			name = newSubCategory.bindFromRequest().get().name;
		} catch(IllegalStateException e) {
			flash("add_sub_null_field", Messages.get("Molim Vas popunite polje u formi."));
			return ok(listaPodKategorija.render(mc));
		}
		name = name.toLowerCase();
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		
		if (SubCategory.findSubCategoryByNameAndMainCategory(name, mc)) {
			return redirect(routes.CategoryController.subCategories(mc.id));
		} else {
			SubCategory.createSubCategory(name, mc);
			Logger.of("category").info("Admin added subcategory " + name);
			flash("add_sub_success", Messages.get("Uspješno ste dodali novu podkategoriju."));
			return redirect(routes.CategoryController.subCategories(mc.id));
		}			
	}	
}
