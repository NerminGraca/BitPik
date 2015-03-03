package controllers;

import models.Product;
import views.html.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller {

	static Form<Product> newProduct = new Form<Product>(Product.class);

	/**
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product
	 * needs to be filled in order to add the Product;
	 */
	public static Result addProduct() {
		String usernameSes = session("username");
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
		String usernameSes = session("username");
		String name = newProduct.bindFromRequest().get().name;
		String desc = newProduct.bindFromRequest().get().desc;
		Double price = newProduct.bindFromRequest().get().price;
		String category = newProduct.bindFromRequest().get().category;
		/**
		 * Ovdje treba izvuci varijablu tipa String iz 
		 * 	<script>
  		 *			$(function() {
    	 *			$( "#cathegory" ).selectmenu();
   		 *			});
  		 *	</script>
  		 *
  		 * String category = Kako primiti varijablu iz Jquery/javascript?
  		 * 
  		 * 	Slijedece dole dvije linije zamijeniti sa ovim iz komentara;
		 *	Product.create(name, desc, price, category);
		 * 	return ok(showProduct.render(usernameSes, name, desc, price));
		 */
		
		Product.create(name, desc, price, category);
		return ok(showProduct.render(usernameSes, name, desc, price, category));
		
		/**
		 * I onda unutar showProduct.html primiti i varijablu (category: String)
		 * i da je iskoristimo unutar body-a showProduct.html 
		 * 	<h2><em>Category </em></h2><br>
				<p>@category</p><br>	
		 */
	}
}
