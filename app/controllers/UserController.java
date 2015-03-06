package controllers;

import java.util.ArrayList;
import java.util.List;

import helpers.HashHelper;
import models.*;
import play.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import views.html.*;

/**
 * 
 * @author Nermin Graca & Nedzad Hamzic & Neldin Dzekovic
 *
 */
public class UserController extends Controller {

	static Form<User> newUser = new Form<User>(User.class);
	static ArrayList<String> adminList = new ArrayList<String>();
		
		public static void insertAdmin(String username)
		{
			if(!adminList.contains(username))
			{
				adminList.add(username);
			}
		}
	/**
	 * Either directs to the index.html with the session name already logged in
	 * or directs to the index.html page with "Unknown" as username;
	 * 
	 * @return
	 */
	public static Result index() {
		List<Product> l = ProductController.findProduct.all();
		
		
/*
		if(receivedVariable.equals("Vozilo")) {
		l = Productcontroller.findProduct.where().eq(category, vozilo).finList()
		}
		
	*/		
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		} 
		return ok(index.render( usernameSes, l, adminList));
		
	}

	/**
	 * Renders the registration.html page;
	 * 
	 * @return
	 */
	public static Result registration() {
		String usernameSes = session("username");
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
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		return ok(login.render(usernameSes, "", ""));
		
	}

	/**
	 * 1. Gets the username, password and email from the form from the
	 * Registration.html page; 2. If the username or email is already in our
	 * database, we redirect to the "/registration.html" 3. Creates the User
	 * using the User.create() method; 4. And redirects to the index.html page;
	 * 
	 * @return
	 */
	public static Result addUser() {
		String username = newUser.bindFromRequest().get().username;
		String password = newUser.bindFromRequest().get().password;
		String email = newUser.bindFromRequest().get().email;
		// Unique 'username' verification
		if (User.finder(username) != null) {
			return ok(registration.render("",
					"Username already taken, please choose another one", ""));
		}
		// Unique 'email' verification
		if (User.emailFinder(email)) {
			return ok(registration.render("", "",
					"Email already in use, please choose another one"));
		}
		User.createSaveUser(username, password, email);
		// automatically puts the 'username' created into the session variable;
		session("username", username);
		return redirect("/");

	}

	/**
	 * 1. Gets the username and password from the form from the Login.html page;
	 * 2. The finder() method finds the User with the entered username; assigns
	 * to the User u; 3. If no User has been found, - redirecting to Failed.html
	 * page; 4. If the User has been found - Checks whether the password is
	 * correct; 5. If the password is wrong - redirecting to Failed.html page;
	 * 6. If the password is correct - redirecting to Index.html page; Note*
	 * Store the username in session variable if the Login is successful;
	 * 
	 * @return
	 */
	public static Result findUser() {
		String hashPass;
		String username = newUser.bindFromRequest().get().username;
		String password = newUser.bindFromRequest().get().password;
		User u = User.finder(username);
		if (u == null) {
			return ok(login.render("", "Username nonexisting", ""));
		} else {
			hashPass = u.password;
		}
		boolean userExists = HashHelper.checkPassword(password, hashPass);
		if (userExists == true) {
			// the username put in the session variable under the key
			// "username";
			session("username", username);
			return redirect("/");
		} else {
			return ok(login.render("", "", "Password is wrong"));
		}
	}

	public static Result showProduct(int id)
	{
		String usernameSes = session("username");
		Product p = ProductController.findProduct.byId(id);
		return ok(showProduct.render(usernameSes, p));
	}
	
	/**
	 * @author Sanela Grcic & Nermin Graca Method Logout - clears current
	 *         session and redirects to index.html
	 * @return redirect to index.html
	 */
	public static Result logout() {
		session().clear();
		
		return redirect(routes.UserController.index());
	}

	/**
	 * For the category "Vozila" - Cars;
	 * The query search for all the products that have the category "Vozila"
	 * And renders the vozila.html page;
	 * @return redners the vozila.html page with the list of cars;
	 */
	public static Result findCars(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Vozila").findList();
		return ok(vozila.render(usernameSes, l));
	}
	
	/**
	 * For the category "Nekretnine" - Real Estate;
	 * The query search for all the products that have the category "Nekretnine"
	 * And renders the nekretnine.html page;
	 * @return renders the nekretnine.html page with the list of realestate;
	 */
	public static Result findRealEstates(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Nekretnine").findList();
		return ok(nekretnine.render(usernameSes, l));
	}

	/**
	 * For the category "Mobilni" - Mobiles;
	 * The query search for all the products that have the category "Mobilni"
	 * And renders the mobilni.html page;
	 * @return renders the mobilni.html page with the list of mobile devices;
	 */
	public static Result findMobiles(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Mobilni uređaji").findList();
		return ok(mobilni.render(usernameSes, l));
	}
	
	/**
	 * For the category "Kompjuteri" - Computers;
	 * The query search for all the products that have the category "Kompjuteri"
	 * And renders the kompjuteri.html page;
	 * @return renders the kompjuteri.html page with the list of computers;
	 */
	public static Result findComputers(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Kompjuteri").findList();
		return ok(kompjuteri.render(usernameSes, l));
	}
	
	/**
	 * For the category "Tehnika" - Technics;
	 * The query search for all the products that have the category "Tehnika"
	 * And renders the tehnika.html page;
	 * @return renders the tehnika.html page with the list of technics;
	 */
	public static Result findTechnics(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Tehnika").findList();
		return ok(tehnika.render(usernameSes, l));
	}

	/**
	 * For the category "Nakit i Satovi" - Jewellery;
	 * The query search for all the products that have the category "Nakit i Satovi"
	 * And renders the nakit.html page;
	 * @return renders the nakit.html page with the list of Jewellery;
	 */
	public static Result findJewellery(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Nakit i satovi").findList();
		return ok(nakit.render(usernameSes, l));
	}

	/**
	 * For the category "Moj dom" - Homes;
	 * The query search for all the products that have the category "Moj dom"
	 * And renders the mojdom.html page;
	 * @return renders the mojdom.html page with the list of homes;
	 */
	public static Result findMyHomes(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Moj dom").findList();
		return ok(mojdom.render(usernameSes, l));
	}

	/**
	 * For the category "Biznis i industrija" - Businesses;
	 * The query search for all the products that have the category "Biznis i industrija"
	 * And renders the biznis.html page;
	 * @return renders the biznis.html page with the list of businesses;
	 */
	public static Result findBusinesses(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Biznis i industrija").findList();
		return ok(biznis.render(usernameSes, l));
	}
	
	/**
	 * For the category "Zivotinje" - Animals;
	 * The query search for all the products that have the category "Zivotinje"
	 * And renders the zivotinje.html page;
	 * @return renders the zivotinje.html page with the list of animals;
	 */
	public static Result findAnimals(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Životinje").findList();
		return ok(zivotinje.render(usernameSes, l));
	}
	
	/**
	 * For the category "Odjeća i obuća" - ClothesShoes;
	 * The query search for all the products that have the category "Odjeća i obuća"
	 * And renders the odjecaobuca.html page;
	 * @return renders the odjecaobuca.html page with the list of clothes and shoes;
	 */
	public static Result findClothesShoes(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Odjeća i obuća").findList();
		return ok(odjecaobuca.render(usernameSes, l));
	}
	
	/**
	 * For the category "Ostale kategorije" - Others;
	 * The query search for all the products that have the category "Ostale kategorije"
	 * And renders the ostalo.html page;
	 * @return renders the ostalo.html page with the list of others;
	 */
	public static Result findOthers(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("category", "Ostale kategorije").findList();
		return ok(ostalo.render(usernameSes, l));
	}

	/**
	 * For the profiles products - that the current logged in user that has published;
	 * The query search for all the products are published by the user logged in;
	 * And renders the profile.html page;
	 * @return renders the profile.html page with the list of products mentioned;
	 */
	public static Result findProfileProducts(){
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List <Product> l = ProductController.findProduct.where().eq("owner.username", usernameSes).findList();
		return ok(profile.render(usernameSes, l));
	}
	
	static Finder<Integer, User> findUser = new Finder<Integer, User>(Integer.class, User.class);
	
	/**
	 * Method list all users registered in database
	 * @return
	 */
	public static Result allUsers() {
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		List<User> userList = findUser.all();
		for (User user: userList)
					{
						if(user.isAdmin)
						{
							insertAdmin(user.username);
						}
					}
		return ok(korisnici.render(usernameSes, userList));
	}
	
	/**
	 * Method shows profile view of single user
	 * @param id
	 * @return
	 */
	public static Result singleUser(int id) {
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		User u = findUser.byId(id);
		List <Product> l = ProductController.findProduct.where().eq("owner.username", u.username).findList();
		return ok(korisnik.render(usernameSes, u, l));
	}
	
	/**
	 * Deletes the User;
	 * @param id
	 * @return
	 */
	public static Result deleteUser(int id) {
		  User.delete(id);
		  return redirect(routes.UserController.allUsers());
	}
	
	/**
	 * Changes the isAdmin attribute of the user under the given id;
	 * @param id
	 * @return
	 */
	public static Result changeAdmin(int id)
	{
		String usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		User u = findUser.byId(id);
		//Sets the admin to !true/false;
		u.setAdmin();
		if(u.isAdmin) {
			insertAdmin(u.username);
		} else {
			if (adminList.contains(u.username)) {
				adminList.remove(u.username);
			}
		}
		//
		List <Product> l = ProductController.findProduct.where().eq("owner.username", u.username).findList();
		return ok(korisnik.render(usernameSes, u, l));
	}
}
