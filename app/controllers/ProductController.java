package controllers;

import java.util.List;

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
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product
	 * needs to be filled in order to add the Product;
	 */
	public static Result addProduct() {
		usernameSes = session("username");
		return ok(addProduct.render(usernameSes));

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
		String desc = newProduct.bindFromRequest().get().desc;
		Double price = newProduct.bindFromRequest().get().price;
		String category = newProduct.bindFromRequest().get().category;
		String availability = newProduct.bindFromRequest().get().availability;
		
		User u = User.finder(usernameSes);
		int id = Product.create(name, desc, price, category, availability, u);
		return redirect("/showProduct/" + id);	
	
	}
	
	/**
	 * Finds the product under the id number;
	 * And sends that product to the editProduct.html page on redering;
	 * Where we will have a new form that we will edit;
	 * @param id
	 * @return
	 */
	public static Result editProduct(int id) {
		usernameSes = session("username");
		Product p = findProduct.byId(id);
		return ok(editProduct.render(usernameSes, p));
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
		String desc = newProduct.bindFromRequest().get().desc;
		Double price = newProduct.bindFromRequest().get().price;
		String category = newProduct.bindFromRequest().get().category;
		String availability = newProduct.bindFromRequest().get().availability;
		
		// sets all the new entered attributes as the original ones from the product;
		// and saves();
		Product p = findProduct.byId(id);
		p.setName(name);
		p.setDesc(desc);
		p.setPrice(price);
		p.setCategory(category);
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
