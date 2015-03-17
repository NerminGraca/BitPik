package controllers;

import java.util.List;

import models.MainCategory;
import models.Product;
import models.User;
import views.html.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller {

	static Form<Product> newProduct = new Form<Product>(Product.class);
	static Finder<Integer, Product> findProduct = new Finder<Integer, Product>(Integer.class, Product.class);
	static String usernameSes;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result showProduct(int id) {
		usernameSes = session("username");
		// 1. Ako nije registrovan da omogucimo prikaz proizvoda;
		if (usernameSes == null) {
			usernameSes = "";
		}
		Product p = ProductController.findProduct.byId(id);
		return ok(showProduct.render(usernameSes, p));
	}
	
	/**
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product
	 * needs to be filled in order to add the Product;
	 */
	public static Result addProduct() {
		usernameSes = session("username");
		// 1. Ako nije registrovan da mu oneomogucimo prikaz addProduct.html;
		if (usernameSes == null) {
			return redirect(routes.Application.index());
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(addProduct.render(usernameSes, mainCategoryList));

	}

	/**
	 * From the filled form takes the necessary values; And creates the object
	 * Product using the constructor; 
	 * Sends the information about the product created to the showProduct.html
	 * page; 
	 * Method creates(saves) the product using the create method from the 
	 * Product model;
	 * 
	 * @return showProducts.html with the necessary variables;
	 */
	public static Result createProduct() {
		usernameSes = session("username");
		String name = newProduct.bindFromRequest().get().name;
		String desc = newProduct.bindFromRequest().get().description;
		Double price = newProduct.bindFromRequest().get().price;
		String category = newProduct.bindFromRequest().get().categoryString;
		String availability = newProduct.bindFromRequest().get().availability;
		MainCategory mc = MainCategory.findMainCategoryByName(category);
		
		User u = User.finder(usernameSes);
		Product p = Product.create(name, desc, price, u, mc, availability);
		return redirect("/showProduct/" + p.id);	
	
	}
	
	/**
	 * Finds the product under the id number;
	 * And sends that product to the editProduct.html page on redering;
	 * Where we will have a new form that we will edit;
	 * @param id
	 * @return
	 */
	/*public static Result editProduct(int id) {
		User currentUser=SessionHelper.getCurrentUser(ctx());
		usernameSes = session("username");
		// 1. Ako nije registrovan da mu oneomogucimo prikaz editProduct.html;
				if (usernameSes == null) {
					return redirect("/");
				}
		// 2. Ako je admin ulogovan, onemogucujemo mu da edituje proizvod;
				if (currentUser.isAdmin==true) {
					return redirect("/");
				}
		// 3. Prosle sve provjere, tj. dozvoljavamo samo registrovanom useru <svog proizvoda> da ga edituje;	
		Product p = findProduct.byId(id);
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(editProduct.render(usernameSes, p, mainCategoryList));
	}*/
	public static Result editProduct(int id) {
	   	 User currentUser=SessionHelper.getCurrentUser(ctx());
	   	 usernameSes = session("username");
	   	 Product p = findProduct.byId(id);
	   	 if(p == null)
	   		 return redirect(routes.Application.index());
	   	 List<MainCategory> mainCategoryList = MainCategory.find.all();
	    //  Ako nije registrovan da mu onemogucimo prikaz editProduct.html;
	   			 if (usernameSes == null) {
	   				 return redirect("/");
	   			 }
	   			 
	   			 if(!currentUser.getUsername().equals(p.owner.getUsername()))
	   			 return redirect(routes.Application.index());
	   	 //  Ako je admin ulogovan, onemogucujemo mu da edituje proizvod;
	   			 if (currentUser.isAdmin==true) {
	   				 return redirect(routes.Application.index());
	   			 }
	   	 //  Prosle sve provjere, tj. dozvoljavamo samo registrovanom useru <svog proizvoda> da ga edituje;    
	   	 return ok(editProduct.render(usernameSes, p, mainCategoryList));
	    }

	/**
	 * Saves the new values of the attributes that are entered 
	 * and overwrites over the ones that were entered before;
	 * @param id
	 * @return redirect("/showProduct/" + id);
	 */
	public static Result saveEditedProduct(int id) {
		//takes the new attributes that are entered in the form;
		usernameSes = session("username");
		String name = newProduct.bindFromRequest().get().name;
		String desc = newProduct.bindFromRequest().get().description;
		Double price = newProduct.bindFromRequest().get().price;
		String category = newProduct.bindFromRequest().get().categoryString;
		String availability = newProduct.bindFromRequest().get().availability;
		
		MainCategory mc = MainCategory.findMainCategoryByName(category);
		
		// sets all the new entered attributes as the original ones from the product;
		// and saves();
		Product p = findProduct.byId(id);
		p.setName(name);
		p.setDesc(desc);
		p.setPrice(price);
		p.setCategory(mc);
		p.setAvailability(availability);
		p.save();
		
		return redirect("/showProduct/" + id);	
		
	}
	
	/**
	 * Deletes the products found under the given id;
	 * @param id
	 * @return
	 */
	public static Result deleteProduct(int id) {
		  Product.delete(id);
		  return redirect(routes.UserController.findProfileProducts());
	}	
	
}
