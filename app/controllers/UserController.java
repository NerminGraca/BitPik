package controllers;

import java.util.List;
import java.util.UUID;

import helpers.AdminFilter;
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
	static String usernameSes;	

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
		String confirmPassword = newUser.bindFromRequest().field("confirmPassword").value();
		String email = newUser.bindFromRequest().get().email;
		// Unique 'username' verification
		if (User.finder(username) != null) {
			Logger.of("user").error("User tried to register with "+ username +" which already exist");
			return ok(registration.render(
					"Korisnicko ime je zauzeto, molimo Vas izaberite drugo!", ""));
		}
		// Unique 'email' verification
		if (User.emailFinder(email)) {
			Logger.of("user").error("User tried to register with "+ email +" which already exist");
			return ok(registration.render("",
					"Email je iskoristen, molimo Vas koristite drugi!"));
		}

		if(!password.equals(confirmPassword))
		{
			Logger.of("user").error("At Registration - Password not confirmed correctly");
			return ok(registration.render("", "Niste ispravno potvrdili lozinku!"));
		}
		
		flash("validate", Messages.get("Primili ste email validaciju."));

		User.createSaveUser(username, password, email);
		Logger.of("user").info("Added a new user "+ username +" (email not verified)");
		// automatically puts the 'username' created into the session variable;
		return redirect(routes.Application.index());

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
			Logger.of("login").error("Login Try - User does not exist, or email not verified yet");
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
			Logger.of("login").info("User " + username + " logged in");
			return redirect(routes.Application.index());
		} else {
			Logger.of("login").error("User " + username + " tried to login with incorrect password");
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
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
			Logger.of("user").warn("Not registered User tried access the profile page");
			return redirect(routes.Application.index());
		}
		List <Product> l = ProductController.findProduct.where().eq("owner.username", usernameSes).findList();
		User u = User.finder(usernameSes);
		return ok(profile.render(l, u));
	}
	
	static Finder<Integer, User> findUser = new Finder<Integer, User>(Integer.class, User.class);
	
	/**
	 * Method list all users registered in database
	 * @return
	 */
	 @Security.Authenticated(AdminFilter.class)
	    public static Result allUsers() {
	   	 usernameSes = session("username");
	   	 List<User> userList = findUser.all();
	   	 return ok(korisnici.render(usernameSes, userList));
	    }
	 
	/**
	 * Method shows profile view of single user
	 * @param id
	 * @return
	 */
	  public static Result singleUser(int id) {
		  User currentUser = SessionHelper.getCurrentUser(ctx());
		  User u = findUser.byId(id);
		  List <Product> l = ProductController.findProduct.where().eq("owner.username", u.username).findList();
		  if(u==null)
			  return redirect (routes.Application.index());
		  if(currentUser==null)
			  return redirect(routes.Application.index());
		  if(u.getUsername().equals(currentUser.getUsername()))
			  return ok(korisnik.render(currentUser, u, l));
		  else
			  return ok(korisnik.render(currentUser, u, l));
	}
	
	/**
	 * Deletes the User;
	 * @param id
	 * @return
	 */
	public static Result deleteUser(int id) {
		  User.delete(id);
		  Logger.of("user").info("Admin deleted a User");
		  return redirect(routes.UserController.allUsers());
	}
		
	/**
	 * Method renders the view in which given user will be edited
	 * @param id
	 * @return
	 */
	public static Result editUser(int id) {
		usernameSes = session("username");
		if ((usernameSes == null)) {
			usernameSes = "";
			return redirect(routes.Application.index());
		}

		User userById = findUser.byId(id);
		User userbyName = findUser.where().eq("username", usernameSes).findUnique();
		if(userbyName.isAdmin==true) {
			Logger.of("user").info("User updated");
			return ok(editUser.render(usernameSes, userById));
		} else 
			if ((userbyName.getUsername()!=userById.getUsername())) {
				Logger.of("user").warn("User "+ usernameSes +" tried to edit/update another user");
				return redirect(routes.Application.index());
			}
			return ok(editUser.render(usernameSes, userById));
	}
	 
	/**
	 * Saves the new values of the attributes that are entered 
	 * and overwrites over the ones that were entered before;
	 * @param id
	 * @return redirect("/korisnik/" + id);
	 */
	public static Result saveEditedUser(int id) {
		
		User user = User.find(id);
		String oldEmail = user.email;
		
		usernameSes = session("username");
		String username = newUser.bindFromRequest().get().username;
		String email = newUser.bindFromRequest().get().email;

		
		user.setUsername(username);
		
		if (oldEmail.equals(email)) {
			user.setEmail(email);
		} else {
			user.setEmail(email);
			user.emailVerified = false;
			String confirmation = UUID.randomUUID().toString();
			user.emailConfirmation = confirmation;
			MailHelper.sendEmailVerification(email,"http://localhost:9000/validateEmail/" + confirmation);
		}
		user.save();
		Logger.of("user").info("User "+ username +" updated");
		session("username", user.username);
		return redirect("/korisnik/" + user.id);		

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
			Logger.of("user").warn("Wrong confirmation string sent in URL to verify email");
			return redirect(routes.Application.index());
		}
		u.confirmation = null;
		u.verified = true;
		u.emailVerified = true;
		u.emailConfirmation = null;
		u.save();
		Logger.of("user").info("User " + u.username+ " verified the email");
		session("username", u.username);
		return redirect(routes.Application.index());
	}
	
	/**
	 * Method is called when velidated user changes it's email,
	 * a verification mail will be sent to him which will verify
	 * given email address
	 * @param r
	 * @return
	 */
	public static Result validateEmail(String r)
	{
		User u = UserController.findUser.where().eq("emailConfirmation", r).findUnique();
		if(r == null || u == null)
		{
			return redirect(routes.Application.index());
		}
		u.emailVerified = true;
		u.emailConfirmation = null;
		u.save();

		return redirect(routes.Application.index());
	}
	
	public static Result changePassword(int id)
	{
		usernameSes = session("username");
		if ((usernameSes == null)) {
			usernameSes = "";
			Logger.of("user").warn("Not registered user tried to change a password");
			return redirect(routes.Application.index());
		}
		User u = findUser.byId(id);
		String password = newUser.bindFromRequest().get().password;
		String confirmPassword = newUser.bindFromRequest().field("confirmPassword").value();
		if(!password.equals(confirmPassword))
		{
			Logger.of("user").error(u.username + " tried to change password - Password not confirmed correctly");
			return ok(changePassword.render(usernameSes, "Niste dobro potvrdili lozinku!", u));
		}
		password = HashHelper.createPassword(password);
		u.setPassword(password);
		u.save();
		Logger.of("user").info("User "+ u.username + " changed their password successfully");
		return redirect("/korisnik/" + id);
	}
	
	/**
	 * Method changes the administrator status of some user
	 * by changing it to either true or false
	 * @param id
	 * @return
	 */
	public static Result changeAdmin(int id) {
		User user = User.find(id);
		boolean admin = newUser.bindFromRequest().get().isAdmin;
		user.isAdmin = admin;
		user.save();
		Logger.of("user").info("An admin user made the User "+ user.username+" an admin");
		return redirect("/korisnik/" + id);
	}
	
	/**
	 *  If user is administrator send him to adminProfile.html
	 * @return adminProfile.html
	 */
	@Security.Authenticated(AdminFilter.class)
    public static Result adminPanel() {
   	  	usernameSes = session("username");
   	  	User u = User.finder(usernameSes);
   	 return ok(adminPanel.render(usernameSes, u));
    }
	
}
