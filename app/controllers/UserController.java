package controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;

import helpers.CurrentUserFilter;
import helpers.AdminFilter;
import helpers.HashHelper;
import helpers.JsonHelper;
import helpers.MailHelper;
import helpers.SessionHelper;
import models.*;
import play.i18n.Messages;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.*;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class UserController extends Controller {
	

	static Form<User> newUser = new Form<User>(User.class);
	static Form<PrivateMessage> sendMessage = new Form<PrivateMessage>(PrivateMessage.class);
	static Form<Comment> postComment = new Form<Comment>(Comment.class);	
	public static final String SESSION_USERNAME = "username";
	public static final String OURHOST = Play.application().configuration().getString("OURHOST");
	
	//Finders
	static Finder<Integer, User> findUser = new Finder<Integer, User>(Integer.class, User.class);
	
	/**
	 * 1. Gets the username, password and email from the form from the
	 * Registration.html page; 2. If the username or email is already in our
	 * database, we redirect to the "/registration.html" 3. Creates the User
	 * using the User.create() method; 4. And redirects to the index.html page;
	 * 
	 * @return
	 */
	public static Result addUser() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
				
		String username;
		String password;
		String confirmPassword;
		String email;
		
		try {
			username = newUser.bindFromRequest().get().username;
			password = newUser.bindFromRequest().get().password;
			confirmPassword = newUser.bindFromRequest().field("confirmPassword").value();
			email = newUser.bindFromRequest().get().email;
		} catch(IllegalStateException e) {
			flash("add_user_null_field", Messages.get("Molim Vas popunite sva polja u formi."));
			return redirect(routes.Application.registration());
		}

		// Null catching
		if (	username == null ||
				password == null ||
				confirmPassword == null ||
				email == null) {
			return redirect(routes.Application.registration());
		}
		
		// Unique 'username' verification
		if (User.finder(username) != null) {
			Logger.of("user").error("User tried to register with "+ username +" which already exist");
			return ok(registration.render("Korisničko ime je zauzeto, molimo Vas izaberite drugo!", ""));
		}
		
		// Unique 'email' verification
		if (User.emailFinder(email)) {
			Logger.of("user").error("User tried to register with "+ email +" which already exist");
			return ok(registration.render("",
					"Email je iskorišten, molimo Vas koristite drugi!"));
		}

		// Password confirmation evaluation
		if(!password.equals(confirmPassword))
		{
			Logger.of("user").error("At Registration - Password not confirmed correctly");
			return ok(registration.render("", "Niste ispravno potvrdili lozinku!"));
		}
		
		flash("validate", Messages.get("Primili ste email validaciju."));
		User.createSaveUser(username, password, email);
		Logger.of("user").info("Added a new user "+ username +" (email not verified)");
		return redirect(routes.Application.index());
	}
	
	public static Result terms() {
		return ok(terms.render());
	}
	
	public static Result addPikStore() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
		String username;
		String password;
		String confirmPassword;
		String email;
		String storeName;
		String address;
		String city;
		String storeCategory;
		
		try {
			username = newUser.bindFromRequest().get().username;
			password = newUser.bindFromRequest().get().password;
			confirmPassword = newUser.bindFromRequest().field("confirmPassword").value();
			email = newUser.bindFromRequest().get().email;
			storeName=newUser.bindFromRequest().get().storeName;
			address=newUser.bindFromRequest().get().address;
			city=newUser.bindFromRequest().get().city;
			storeCategory=newUser.bindFromRequest().get().categoryString;
		} catch(IllegalStateException e) {
			flash("add_user_null_field", Messages.get("Molim Vas popunite sva polja u formi."));
			return redirect(routes.Application.registrationPikStore());
		}
		MainCategory mainCat = MainCategory.findMainCategoryByName(storeCategory);

		// Null catching
		if (	username == null ||
				password == null ||
				confirmPassword == null ||
				email == null || storeName==null || address==null || city==null){
			return redirect(routes.Application.registrationPikStore());//novi redirect
		}
		
		// Unique 'username' verification
		if (User.finder(username) != null) {
			Logger.of("user").error("User tried to register with "+ username +" which already exist");
			return ok(registrationPikStore.render(
					"Korisnicko ime je zauzeto, molimo Vas izaberite drugo!", "",MainCategory.allMainCategories()));
		}
		
		// Unique 'email' verification
		if (User.emailFinder(email)) {
			Logger.of("user").error("User tried to register with "+ email +" which already exist");
			return ok(registrationPikStore.render("",
					"Email je iskoristen, molimo Vas koristite drugi!",MainCategory.allMainCategories()));
		}

		// Password confirmation evaluation
		if(!password.equals(confirmPassword))
		{
			Logger.of("user").error("At Registration - Password not confirmed correctly");
			return ok(registrationPikStore
					.render("", "Niste ispravno potvrdili lozinku!",MainCategory.allMainCategories()));
		}
		
		flash("validate", Messages.get("Primili ste email validaciju."));
		User.createPikStore(username, password, email,storeName,address,city,mainCat);
		Logger.of("pikStore").info("Added a new Pik Store "+ username +" (email not verified)");
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
		String username;
		String password;
		
		if (!request().accepts("text/html")) {
			return JsonController.login();
		}
		
		try {
			username = newUser.bindFromRequest().get().username;
			password = newUser.bindFromRequest().get().password;
		} catch(IllegalStateException e) {
			flash("login_null_field", Messages.get("Molim Vas popunite sva polja u formi"));
			return redirect(routes.Application.login());
		}
		
		
		// Null catching
		if (username == null || password == null) {
			return redirect(routes.Application.login());
		}
		
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
		if (!request().accepts("text/html")) {
			return ok(JsonHelper.jsonUser(u));
		}

		if (userExists && u.verified) {
			
			if(username.equals("blogger")){
				session(SESSION_USERNAME, username);
				Logger.of("login").info("User " + username + " logged in");
				return redirect(routes.BloggerController.showBlog());
			}
			// the username put in the session variable under the key
			// "username";
			session(SESSION_USERNAME, username);
			Logger.of("login").info("User " + username + " logged in");
			flash("login_success", Messages.get("Dobrodosli "+ u.username));
			return redirect(routes.Application.index());
		} else {
			Logger.of("login").error("User " + username + " tried to login with incorrect password");
			return ok(login.render("", "Password je netačan"));
		}
	}

	/**
	* For the profiles products - that the current logged in user that has published;
	* The query search for all the products are published by the user logged in;
	* And renders the profile.html page;
	* @return renders the profile.html page with the list of products mentioned;
	*/
	//@Security.Authenticated(SessionHelper.class)
	public static Result findProfileProducts(){
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		
		// Null Catching
		if (currentUser == null) {
			Logger.of("user").warn("Not registered User tried access the profile page");
			return redirect(routes.Application.index());
		}
		List <Product> productList = ProductController.findProduct.where().eq("owner.username", currentUser.username).eq("isSold", false).findList();
		User u = User.finder(currentUser.username);

		
		return ok(profile.render(productList, u));

	}	
	
	/**
	* This method lists all the bought items of the User logged in;
	* If no products where bought by the user we inform him about it; 
	* 
	* @return Result;
	*/
	public static Result find_bought_products() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());		
		//Null Catching
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}		
		// List of products that the current logged in User has bought;
		List <Product> productList = ProductController.findProduct.where().eq("buyerUser", currentUser).findList();
		if (productList.isEmpty()) {
			flash("no_bought_products", Messages.get("Vi jos uvijek nemate kupljenih proizvoda"));

		}
		if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonProductList(productList));
			array.add(JsonHelper.jsonUser(currentUser));
			return ok(array);
		}
		return ok(boughtproducts.render(productList, currentUser));
	}
	
	/**
	 * This method lists all the items of the User logged in that he has sold;
	 * If no products where sold by the user we inform him about it; 
	 * 
	 * @return Result;
	 */
	public static Result findSoldProducts() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		//Null Catching
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		// List of products that the current logged in User has sold;

		List <Product> productList = ProductController.findProduct.where().eq("owner", currentUser).eq("isSold", true).findList();
		if (productList.isEmpty()) {
			flash("no_sold_products", Messages.get("Vi jos uvijek nemate prodatih proizvoda"));

		}
		if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonProductList(productList));
			array.add(JsonHelper.jsonUser(currentUser));
			return ok(array);
		}
		return ok(soldproducts.render(productList, currentUser));
	}	
	
	/**
	* Method list all users registered in database
	* @return
	*/
	@Security.Authenticated(AdminFilter.class)
	public static Result allUsers() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
	   	 List<User> userList = findUser.all();
	   	if (!request().accepts("text/html")) {
		 	 return ok(JsonHelper.jsonUserList(userList));
	   	 }
	   	 return ok(korisnici.render(userList));
	}
	

	public static Result allPikStores() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
	   	 List<User> stores = findUser.where().eq("isPikStore",true).findList();
	   	if (!request().accepts("text/html")) {
		 	 return ok(JsonHelper.jsonUserList(stores));
	   	 }
	   	 return ok(pikRadnje.render(u,stores));
	}
	
	public static Result pikStoresByCategory(int categoryId){
		User u = SessionHelper.getCurrentUser(ctx());
		MainCategory mainCategory=MainCategory.findMainCategory(categoryId);
		List<User>stores=findUser.where().eq("storeCategory.name", mainCategory.name).findList();
		return ok(pikRadnje.render(u,stores));
	}
	 
	/**
	* Method shows profile view of single user
	* @param id
	* @return
	*/
	public static Result singleUser(int id) {
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		User u = findUser.byId(id);
		List <Product> productList = ProductController.findProduct.where().eq("owner.username", u.username).findList();
//		if(currentUser==null){
//			return redirect(routes.Application.index());
//		}
		if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonUser(currentUser));
			array.add(JsonHelper.jsonUser(u));
			array.add(JsonHelper.jsonProductList(productList));
			return ok(array);
	   	 }
		return ok(korisnik.render(currentUser, u, productList));	  
	}
	
	/**
	 * Deletes the User;
	 * @param id
	 * @return
	 */
	public static Result deleteUser(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		  User.delete(id);
		  Logger.of("user").info("Admin deleted a User");
		  flash("delete_user_success",  Messages.get("Uspješno ste izbrisali Usera"));
		  if (!request().accepts("text/html")) {
				return ok();
		  }
		  return redirect(routes.UserController.allUsers());
	}
		
	/**
	* Method renders the view in which given user will be edited
	* @param id
	* @return
	*/
	@Security.Authenticated(CurrentUserFilter.class)
	public static Result editUser(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}

		User userById = findUser.byId(id);
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (userById.equals(currentUser)) {
			return ok(editUser.render(userById, currentUser));
		} else {
			return redirect(routes.Application.index());	
		}				
	}
	 
	/**
	 * Saves the new values of the attributes that are entered 
	 * and overwrites over the ones that were entered before;
	 * @param id
	 * @return redirect("/korisnik/" + id);
	 */
	public static Result saveEditedUser(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
		User currentUser = SessionHelper.getCurrentUser(ctx());
		User user = User.find(id);
		String oldEmail = user.email;		
		
		String username;
		String email;
		
		try {
			username = newUser.bindFromRequest().get().username;
			email = newUser.bindFromRequest().get().email;
		} catch(IllegalStateException e) {
			flash("edit_user_null_field", Messages.get("Molim Vas popunite sva polja u formi"));
			return ok(editUser.render(user, currentUser));
		}
		
		user.setUsername(username);
		
		if (oldEmail.equals(email)) {
			user.setEmail(email);
			flash("edit_user_success", Messages.get("Uspješno ste izmijenili profil"));
		} else {
			user.setEmail(email);
			user.emailVerified = false;
			String confirmation = UUID.randomUUID().toString();
			user.emailConfirmation = confirmation;
			MailHelper.sendEmailVerification(email, UserController.OURHOST + "/validateEmail/" + confirmation);
			flash("validate", Messages.get("Primili ste email validaciju."));
		}
		
		user.save();
		Logger.of("user").info("User with "+ oldEmail +" updated. NEW : ["+ user.username +", "+ user.email +"]");
		session(SESSION_USERNAME, user.username);
		return redirect("/profile" );
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
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
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
		session(SESSION_USERNAME, u.username);
		if (!request().accepts("text/html")) {
			return ok();
	   	 }
		return redirect(routes.Application.index());
	}
	
	/**
	 * Method is called when validated user changes it's email,
	 * a verification mail will be sent to him which will verify
	 * given email address
	 * @param r
	 * @return
	 */
	public static Result validateEmail(String r)
	{
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		User u = UserController.findUser.where().eq("emailConfirmation", r).findUnique();
		if(r == null || u == null)
		{
			return redirect(routes.Application.index());
		}
		u.emailVerified = true;
		u.emailConfirmation = null;
		u.save();
		if (!request().accepts("text/html")) {
			return ok();
	   	 }
		flash("validate", Messages.get("Novi email verifikovan"));
		return redirect(routes.Application.index());
	}
	
	@Security.Authenticated(CurrentUserFilter.class)
	public static Result changePassword(int id)
	{
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		
		User u = findUser.byId(id);
		String password;
		String confirmPassword;
		
		try {
			password = newUser.bindFromRequest().get().password;
			confirmPassword = newUser.bindFromRequest().field("confirmPassword").value();
		} catch(IllegalStateException e) {
			flash("chng_pass_null_field", Messages.get("Molimo Vas da popunite sva polja u formi."));
			return ok(changePassword.render(u));
		}
		
		if (!password.equals(confirmPassword))
		{
			Logger.of("user").error(u.username + " tried to change password - Password not confirmed correctly");
			flash("chng_pass_not_confirmed", Messages.get("Niste dobro potvrdili šifru."));
			return ok(changePassword.render(u));
		}
		password = HashHelper.createPassword(password);
		u.setPassword(password);
		u.save();
		Logger.of("user").info("User "+ u.username + " changed their password successfully");
		flash("chng_pass_success", Messages.get("Uspješno ste zamijenili vašu šifru."));
		return redirect("/korisnik/" + id);
	}
	
	public static Result changePasswordPage() {
		
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		return ok(changePassword.render(us));
	
	}
	
	/**
	 * Method changes the administrator status of some user
	 * by changing it to either true or false
	 * @param id
	 * @return
	 */
	public static Result changeAdmin(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User user = User.find(id);
		boolean admin = newUser.bindFromRequest().get().isAdmin;
		user.isAdmin = admin;
		user.save();
		Logger.of("user").info("An admin user made the User "+ user.username+" an admin");
		if (!request().accepts("text/html")) {
				return ok(JsonHelper.jsonUser(user));
	   	 }
		return redirect("/korisnik/" + id);
	}
	
	/**
	 * If user is administrator send him to adminProfile.html
	 * @return adminProfile.html
	 */
	@Security.Authenticated(AdminFilter.class)
    public static Result adminPanel() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		//Null Catching
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		List<Product> specialProducts = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", true).findList();
		List<Product> products = ProductController.findProduct.where().eq("isRefunding", true).findList();
   	  	if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonProductList(specialProducts));
			array.add(JsonHelper.jsonUser(currentUser));
			array.add(JsonHelper.jsonProductList(products));
			return ok(array);
	   	 }

   	 return ok(adminPanel.render(specialProducts, currentUser, products));

    }
	
	/**
	* Upload image for User profile, and show picture on user /profile.html. 
	* If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
	* If file size is bigger then 2MB, redirect user on profile without uploading image.
	* @return
	*/
	public static Result saveFile(){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
		//checks if picture exists in base, if it does deletes it then uploads new picture
		final String deletePath = "." + File.separator 
				+ "public" + File.separator;
		String s = findUser.byId(u.id).imagePath;
		String defaultPic = "images" + File.separator + "profilePicture" + File.separator + "profileimg.png";
		
		if (s != null && !s.equals(defaultPic)){
			File d = new File(deletePath + s);
			d.delete();
		}
			
		int userID = u.id;
   	  	//creating path where we are going to save image
		final String savePath = "." + File.separator 
				+ "public" + File.separator + "images" 
				+ File.separator + "profilePicture" + File.separator;
		
		//it takes uploaded information  
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("image");
		if (filePart == null){
			 flash("error",  Messages.get("Niste uploadovali sliku"));
			 return redirect("/profile");
		}
		File image = filePart.getFile();
		//it takes extension from image that is uploaded
		String extension = filePart.getFilename().substring(filePart.getFilename().lastIndexOf('.'));
		extension.trim();
		
		//If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
		if(	   !extension.equalsIgnoreCase(".jpeg") 
			&& !extension.equalsIgnoreCase(".jpg")
			&& !extension.equalsIgnoreCase(".png") ){
			
			flash("error",  Messages.get("Slika ne smije biti veća od 2 MB"));
			Logger.of("user").warn( u.username + " tried to upload an image that is not valid.");
			return redirect("/profile");
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){
			flash("error",  Messages.get("Image size not valid"));
			Logger.of("user").warn( u.username + " tried to upload an image that is bigger than 2MB.");
			return redirect("/profile");
		}
		
		//creating image name from user id, and take image extension, than move image to new location
		try {
			File profile = new File(savePath + userID + extension);
			Files.move(image, profile );		
			String assetsPath = "images" 
					+ File.separator + "profilePicture" + File.separator + profile.getName();
			u.imagePath = assetsPath;
			u.save();
		} catch (IOException e) {
			Logger.of("user").error( u.username + " failed to upload an image to his profile page.");
			e.printStackTrace();
		}
		flash("upload_img_success",  Messages.get("Uspješno ste objavili sliku"));
		if (!request().accepts("text/html")) {
			return ok(JsonHelper.jsonUser(u));
	   	 }
		return redirect("/profile");
	}
	
	/**
	 * Method finds one product and sends
	 * it to view.
	 * @param id
	 */
	
	public static Result showPurchase(int id) {
		if (!request().accepts("text/html")) {
			ObjectNode num = Json.newObject();
			num.put("id", id);
			return ok(num);
	   	}
		return ok(purchase.render(id));
	}
	
	/**
	 * Method integrates PayPal with application, using access token.
	 * It adds amount, currency, payer, description, intent and state
	 * for payment. 
	 * @param id
	 * @return
	 */

	public static Result purchaseProcessing(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Product p = Product.find.byId(id);
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		try{
			String payPalSecretKey1 = Play.application().configuration().getString("payPalSecretKey1");
			String payPalSecretKey2 = Play.application().configuration().getString("payPalSecretKey2");
			String accessToken = new OAuthTokenCredential(payPalSecretKey1, payPalSecretKey2).getAccessToken();
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			Amount amount = new Amount();

			// We put the amount in USD and convert it to a String;
			amount.setTotal(p.getPriceinStringinUSD());
			amount.setCurrency("USD");
			Transaction transaction = new Transaction();
			transaction.setDescription("Čestitamo, još ste samo nekoliko koraka od kupovine proizvoda '" + p.name +
										"' sa sljedećim opisom : '" + p.description + "'");

			transaction.setAmount(amount);
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);
			
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			
			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setState("Bosnia and Herzegovina");
			payment.setTransactions(transactions);
			
			RedirectUrls redirectUrls = new RedirectUrls();
			flash("buy_fail",  Messages.get("Paypal transakcija nije uspjela"));

			redirectUrls.setCancelUrl(OURHOST + "/showProduct/"+ id);
			redirectUrls.setReturnUrl(OURHOST + "/purchasesuccess/"+id);

			payment.setRedirectUrls(redirectUrls);
			Payment createdPayment = payment.create(apiContext);
			Logger.debug(createdPayment.toJSON());
			List<Links> links = createdPayment.getLinks();
			Iterator<Links> itr = links.iterator();
			while(itr.hasNext()){
				Links link = itr.next();
				if(link.getRel().equals("approval_url"))
				{
//					if (!request().accepts("html/text")){
//						ObjectNode num = Json.newObject();
//						num.put("id", id);
//						return ok(num);
//				   	}
					return redirect(link.getHref());
				}
			}
			
		} catch(PayPalRESTException e)
		{
			Logger.warn(e.getMessage());
		}
		return TODO;		
	}
	
	/**
	 * Method validates payment with existing user.
	 * After binding from dynamic form it uses access token
	 * to transact payment.
	 * @param id
	 */
	
	public static Result purchaseSuccess(int id ) {
		
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Product p = Product.find.byId(id);
		String paymentId = null;
		String payerId = null;
		String token = null;
		String accessToken = null;

		
		String payPalSecretKey1 = Play.application().configuration().getString("payPalSecretKey1");
		String payPalSecretKey2 = Play.application().configuration().getString("payPalSecretKey2");

		
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		try {
			DynamicForm paypalReturn = Form.form().bindFromRequest();
		    paymentId = paypalReturn.get("paymentId");
			payerId = paypalReturn.get("PayerID");
			token = paypalReturn.get("token");

			 accessToken = new OAuthTokenCredential("ARl5dVTUzOXK0p7O1KgG5ZpLg-E9OD5CgoqNXMuosC3efZWeZlBPODxDV6WeIFfJnS5atklHgrt8lMVO", 
					"EDrDunRMuM_aAbbILclme0f4dfL2kZ1OGrS8NVDIjWwN6N8G9s-vF0udi97t2rcP8_HiiGgkUL9XBhoS").getAccessToken();
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			Payment payment = Payment.get(accessToken, paymentId);
			

			
			accessToken = new OAuthTokenCredential(payPalSecretKey1, payPalSecretKey2).getAccessToken();
			
			apiContext.setConfigurationMap(sdkConfig);
			
			//Payment payment = Payment.get(accessToken, paymentId);			

			//payment.execute(apiContext, paymentExecution);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		
		return ok(payPalValidation.render(p, u, payerId, paymentId, token, accessToken));
	}
	
	/**
	 * Method checks if user exists, and if does
	 * it calls saveMessage method with same id for message
	 * @param id
	 */
	public static Result sendMessage(int id)
	{
		
		User u = SessionHelper.getCurrentUser(ctx());
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		if(u == null)
		{
			return redirect(routes.Application.index());
		}
		User receiver = User.find(id);
		if (!request().accepts("text/html")) {
			return JsonController.sendMessage(id);
	   	}
		return ok(message.render(u, receiver));
	}
	
	/**
	 * Method that binds values from sendMessage form
	 * and creates new message using create method
	 * in PrivateMessage model.
	 * @param id
	 */
	public static Result saveMessage(int id)
	{
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User sender = SessionHelper.getCurrentUser(ctx());
   	  	User receiver = findUser.byId(id);
   	  	String content;
		try {
			content = sendMessage.bindFromRequest().get().content;
		} catch (Exception e) {
			flash("message_fail", Messages.get("Greška. Niste poslali poruku"));
			return redirect(routes.UserController.singleUser(id));
		}
   	  	PrivateMessage privMessage = PrivateMessage.create(content, sender, receiver);
   	  	receiver.privateMessage.add(privMessage);
	  	receiver.save();
	  	if(receiver.privateMessage.contains(privMessage))
	  	{
	  		flash("message_success", Messages.get("Poslali ste poruku"));
	  	}
	  	if(sender == null)
	  	{
	  		return redirect(routes.Application.index());
	  	}
	  	String message = "Dobili ste privatnu poruku od korisnika " + sender.username + 
	  			": \n" + content;
	  	MailHelper.sendNewsletter(receiver.email, message);
   	  	return redirect("/allMessages");
	}
	
	/**
	 * Method sends two lists to view, list of received messages
	 * and list of sent messages for one user.
	 */
	public static Result allMessages()
	{
		
		User u = SessionHelper.getCurrentUser(ctx());
		User us = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(us != null && us.username.equals("blogger")){
			return ok(blog.render(bloggerList,us));
		}
		if(u == null)
	  	{
	  		return redirect(routes.Application.index());
	  	}
		List<PrivateMessage> messagesReceived = PrivateMessage.find.where().eq("receiver.id", u.id).findList();
		List<PrivateMessage> messagesSent = PrivateMessage.find.where().eq("sender.id", u.id).findList();
		if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonUser(u));
			array.add(JsonHelper.jsonMessageList(messagesReceived));
			array.add(JsonHelper.jsonMessageList(messagesSent));
			return ok(array);
	   	}
		return ok(allMessages.render(u, messagesReceived, messagesSent));

	}
	
	/**
	 * 
	 * @param id
	 * @param payerId
	 * @param paymentId
	 * @param token
	 * @param accessToken
	 * @return
	 */
	public static Result showSellingProduct(int id, String payerId, String paymentId, String token, String accessToken) {
		
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
			DynamicForm paypalReturn = Form.form().bindFromRequest();
			
		String payPalSecretKey1 = Play.application().configuration().getString("payPalSecretKey1");
		String payPalSecretKey2 = Play.application().configuration().getString("payPalSecretKey2");
		
		try {
			
			//DynamicForm paypalReturn = Form.form().bindFromRequest();
			
			accessToken = new OAuthTokenCredential(payPalSecretKey1, payPalSecretKey2).getAccessToken();

			Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			Payment payment = Payment.get(accessToken, paymentId);
			
			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(payerId);
			
			payment.execute(apiContext, paymentExecution);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return redirect(OURHOST + "/buyingAProduct/" +id +"/"+ token);
	}	
	
	public static Result newsletter() throws ParseException{
		String message = "<h2>Novosti sa BitPik-a!</h2><br><h3>Novi oglasi koji bi vas mogli zanimati:</h3><br>";
		List<User> users = User.find.all();
		for(User u: users){
			List<Product>newsletterProducts = new ArrayList<Product>();
			for (Newsletter n: u.newsletter){
				Logger.error("n.string" + n.searchString);
				Product p =  Product.find.where("(UPPER(name) LIKE UPPER('%"+n.searchString+"%')) AND ((isSold) LIKE (false))").findUnique();
				newsletterProducts.add(p);
				/*	SimpleDateFormat sdf = new SimpleDateFormat();
				Date pDate;
				Date nDate;
				
				
					pDate = sdf.parse(p.publishedDate);
					nDate = sdf.parse(n.createdDate.toString());
					Logger.error("Datum 1: " + pDate);
					Logger.error("Datum 2: " + nDate);
					if(pDate.compareTo(nDate)==0){
						newsletterProducts.add(p);
					}
				*/
			}
			
			if(newsletterProducts.size() > 0)
			{
				Logger.error("prviString" + newsletterProducts.get(0).name);
				for(Product p: newsletterProducts){
				if (p != null) {
					message += p.name + ", opis: " + p.description + 
							", cijena <strong>" + p.price + "</strong> " + ", objavio korisnik " +
							p.owner.username + " dana " + p.publishedDate.toString() + "<br>";
					} else {
						return redirect("/korisnici");
					}
				}
			}
			if (u.newsletter.size() > 0) {
				MailHelper.sendNewsletter(u.email, message);
			}
		}
		return redirect("/korisnici");
	}

}
