package controllers;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;

import models.*;
import helpers.SessionHelper;
import play.mvc.Controller;
import play.mvc.Result;
import views.*;
import views.html.*;
import models.Product;
import models.Statistics;

/**
 * This is the Statistics controller;
 * Which controls the function of all of the products and their 
 * statistic information, as in how many times has an article 
 * been clicked on, been commented on, and many more statistics 
 * regarding an article published on the web page;
 * @author Necko
 *
 */
public class StatsController extends Controller {

	/**
	 * Method showStats() gets us the page statsproducts
	 * where we give the list of all products to the User
	 * who has published all of the products, it does not matter
	 * whether he is still selling them or has already sold them.
	 * As they will all be listed with their statistics of 
	 * each of the item.
	 * 
	 * @return
	 */
	public static Result showStats() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// Null Catching
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		List<Product> allProducts = ProductController.findProduct.where().eq("owner.username", currentUser.username).findList();
		
		// Going through the products and setting the displaying the adequate Strings on stats page;
		Iterator<Product> iter = allProducts.iterator();
		while (iter.hasNext()) {
				Product p = iter.next();
				if (p.isSold) {
					p.statsProducts.setIsItSold("DA");
				} else {
					p.statsProducts.setIsItSold("NE");
				}
				
				if (p.isSpecial) {
					p.statsProducts.setIsItSpecial("DA");
				} else {
					p.statsProducts.setIsItSpecial("NE");
				}
			p.statsProducts.setNoOfClicksS(String.valueOf(p.statsProducts.noOfClicks));
		
		}
		
				
		return ok(statsproducts.render(currentUser, allProducts));

	}
	
}
