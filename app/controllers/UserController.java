package controllers;

import java.util.ArrayList;
import java.util.List;

import helpers.HashHelper;
import helpers.MailHelper;
import models.*;
import play.i18n.Messages;
import play.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import views.html.*;

public class UserController extends Controller {

	static Form<User> newUser = new Form<User>(User.class);
	static ArrayList<String> adminList = new ArrayList<String>();
	static String usernameSes;	
	
	/**
	 * Either directs to the index.html with the session name already logged in
	 * or directs to the index.html page with "Unknown" as username;
	 * 
	 * @return
	 */
	public static Result index() {
		List<Product> productList = ProductController.findProduct.all();
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		} 
		return ok(index.render( usernameSes, productList, adminList, mainCategoryList));
		
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
			return ok(registration.render(
					"Username already taken, please choose another one", ""));
		}
		// Unique 'email' verification
		if (User.emailFinder(email)) {
			return ok(registration.render("",
					"Email already in use, please choose another one"));
		}
		
		User.createSaveUser(username, password, email);
		// automatically puts the 'username' created into the session variable;
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
		//if not found or not verified email, after login;
		if (u == null || u.verified==false) {
			return ok(login.render("Ne postoji korisnik ili email nije verificiran", ""));
		} else {
			hashPass = u.password;
		}
		boolean userExists = HashHelper.checkPassword(password, hashPass);
		// if verified and matching passwords;
		if (userExists == true && u.verified==true) {
			// the username put in the session variable under the key
			// "username";
		session("username", username);
			return redirect("/");
		} else {
			return ok(login.render("", "Password je netacan"));
		}
	}

	/**
	 * For the profiles products - that the current logged in user that has published;
	 * The query search for all the products are published by the user logged in;
	 * And renders the profile.html page;
	 * @return renders the profile.html page with the list of products mentioned;
	 */
	public static Result findProfileProducts(){
		User currentUser=SessionHelper.getCurrentUser(ctx());
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
			return redirect("/");
		}
		List <Product> l = ProductController.findProduct.where().eq("owner.username", usernameSes).findList();
		User u = User.finder(usernameSes);
		return ok(profile.render(usernameSes, l, u, adminList));
	}
	
	static Finder<Integer, User> findUser = new Finder<Integer, User>(Integer.class, User.class);
	
	/**
	 * 
	 * @param username
	 */
	public static void insertAdmin(String username)	{
		if(!adminList.contains(username)) {
				adminList.add(username);
		}
	}
	
	/**
	 * Method list all users registered in database
	 * @return
	 */
	public static Result allUsers() {
		List<Product> productList = ProductController.findProduct.all();
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		usernameSes = session("username");
		//1. Ne logovan korisnik; - vraca ga na pocetnu stranicu;
		if (usernameSes == null) {
			usernameSes = "";
			return ok(index.render(usernameSes, productList, adminList, mainCategoryList));
		}
		// 2. Ako je obicni korisnik tj. ako nije admin; - vraca ga na pocetnu stranicu;
		User u = findUser.where().eq("username", usernameSes).findUnique();
		if (u.isAdmin == false) {
			return ok(index.render(usernameSes, productList, adminList, mainCategoryList));
		}
		// 3. Ako je prosao prethodne if-ove; Slucaj ako je stvarno admin, prikazuju se svi korisnici;
		List<User> userList = findUser.all();
		for (User user: userList)
					{
						if(user.isAdmin)
						{
							insertAdmin(user.username);
						}
					}
		return ok(korisnici.render(usernameSes, userList, adminList));
	}
	
	/**
	 * Method shows profile view of single user
	 * @param id
	 * @return
	 */
	public static Result singleUser(int id) {
		usernameSes = session("username");
		User u = findUser.byId(id);
		String username=u.username;
		List <Product> l = ProductController.findProduct.where().eq("owner.username", u.username).findList();
		if ((usernameSes == null)) {
			
			return redirect("/");
		}
		User userbyName = findUser.where().eq("username", usernameSes).findUnique();
		
		
		return ok(korisnik.render(usernameSes, u, l, adminList));
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
	
	
	
	public static Result editUser(int id) {
		usernameSes = session("username");
		if ((usernameSes == null)) {
			usernameSes = "";
			return redirect("/");
		}
		User userById = findUser.byId(id);
		User userbyName = findUser.where().eq("username", usernameSes).findUnique();
		if(userbyName.isAdmin==true)
			return ok(editUser.render(usernameSes, userById, adminList, userbyName.isAdmin));
		
		
		else 
			if ((userbyName.getUsername()!=userById.getUsername())) {
				return redirect("/");
				}
		
		
		
		return ok(editUser.render(usernameSes, userById, adminList, userbyName.isAdmin));
	}
	
	/**
	 * Saves the new values of the attributes that are entered 
	 * and overwrites over the ones that were entered before;
	 * @param id
	 * @return redirect("/showProduct/" + id);
	 */
	public static Result saveEditedUser(int id) {
		usernameSes = session("username");
		String username = newUser.bindFromRequest().get().username;
		String email = newUser.bindFromRequest().get().email;
		String password = newUser.bindFromRequest().get().password;
		boolean isAdmin = newUser.bindFromRequest().get().isAdmin;
		password = HashHelper.createPassword(password);
		User u = findUser.byId(id);
		u.setUsername(username);
		u.setEmail(email);
		u.setPassword(password);
		u.setAdmin(isAdmin);
		u.save();
		
		return redirect("/korisnik/" + id);	
		
	}
	
	/**
	 * After the click on the - link has been send to registered user;
	 * We change the verified boolean to true;
	 * We null the confirmation String;
	 * @param r
	 * @return return redirect("/");
	 */
	public static Result confirmEmail(String r)
	{
		User u = UserController.findUser.where().eq("confirmation", r).findUnique();
		if(r == null || u == null)
		{
			return redirect("/");
		}
		u.confirmation = null;
		u.verified = true;
		u.save();

		session("username", u.username);
		return redirect("/");
	}
	
}
