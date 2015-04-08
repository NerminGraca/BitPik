package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.io.Files;

import helpers.CurrentUserFilter;
import helpers.AdminFilter;
import helpers.HashHelper;
import helpers.MailHelper;
import helpers.SessionHelper;
import models.*;
import play.i18n.Messages;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
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
	static String usernameSes;	
	private static final String SESSION_USERNAME = "username";
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

		if (	username == null ||
				password == null ||
				confirmPassword == null ||
				email == null) {
			return redirect(routes.Application.registration());
		}
		
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
		
		try {
			username = newUser.bindFromRequest().get().username;
			password = newUser.bindFromRequest().get().password;
		} catch(IllegalStateException e) {
			flash("login_null_field", Messages.get("Molim Vas popunite sva polja u formi"));
			return redirect(routes.Application.login());
		}
				
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
		if (userExists && u.verified) {
			// the username put in the session variable under the key
			// "username";
			session(SESSION_USERNAME, username);
			Logger.of("login").info("User " + username + " logged in");
			flash("login_success", Messages.get("Dobrodosli "+ u.username));
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
		usernameSes = session(SESSION_USERNAME);
		if (usernameSes == null) {
			Logger.of("user").warn("Not registered User tried access the profile page");
			return redirect(routes.Application.index());
		}
		List <Product> l = ProductController.findProduct.where().eq("owner.username", usernameSes).eq("isSold", false).findList();
		User u = User.finder(usernameSes);
		return ok(profile.render(l, u));
	}	
	
	/**
	 * This method lists all the bought items of the User logged in;
	 * If no products where bought by the user we inform him about it; 
	 * 
	 * @return Result;
	 */
	public static Result find_bought_products() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// List of products that the current logged in User has bought;
		List <Product> l = ProductController.findProduct.where().eq("buyerUser", currentUser).findList();
		if (l.isEmpty()) {
			flash("no_bought_products", Messages.get("Vi jos uvijek nemate kupljenih proizvoda"));
		}
		return ok(boughtproducts.render(l, currentUser));
	}
	
	/**
	 * This method lists all the items of the User logged in that he has sold;
	 * If no products where sold by the user we inform him about it; 
	 * 
	 * @return Result;
	 */
	public static Result findSoldProducts() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// List of products that the current logged in User has sold;
		List <Product> l = ProductController.findProduct.where().eq("owner", currentUser).eq("isSold", true).findList();
		if (l.isEmpty()) {
			flash("no_sold_products", Messages.get("Vi jos uvijek nemate prodatih proizvoda"));
		}
		return ok(soldproducts.render(l, currentUser));
	}
	
	
	/**
	* Method list all users registered in database
	* @return
	*/
	@Security.Authenticated(AdminFilter.class)
	public static Result allUsers() {
	   	 List<User> userList = findUser.all();
	   	 return ok(korisnici.render(userList));
	}
	 
	/**
	* Method shows profile view of single user
	* @param id
	* @return
	*/
	@Security.Authenticated(CurrentUserFilter.class)
	public static Result singleUser(int id) {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		User u = findUser.byId(id);
		List <Product> l = ProductController.findProduct.where().eq("owner.username", u.username).findList();
		if(currentUser==null){
			return redirect(routes.Application.index());
		}
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
		  flash("delete_user_success",  Messages.get("Uspjesno ste izbrisali Usera"));
		  return redirect(routes.UserController.allUsers());
	}
		
	/**
	* Method renders the view in which given user will be edited
	* @param id
	* @return
	*/
	@Security.Authenticated(CurrentUserFilter.class)
	public static Result editUser(int id) {

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
		
		User currentUser = SessionHelper.getCurrentUser(ctx());
		User user = User.find(id);
		String oldEmail = user.email;		
		usernameSes = session(SESSION_USERNAME);
		
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
			flash("edit_user_success", Messages.get("Uspjesno ste izmijenili profil"));
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
		User u = UserController.findUser.where().eq("emailConfirmation", r).findUnique();
		if(r == null || u == null)
		{
			return redirect(routes.Application.index());
		}
		u.emailVerified = true;
		u.emailConfirmation = null;
		u.save();

		flash("validate", Messages.get("Novi email verifikovan"));
		return redirect(routes.Application.index());
	}
	
	@Security.Authenticated(CurrentUserFilter.class)
	public static Result changePassword(int id)
	{
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
			flash("chng_pass_not_confirmed", Messages.get("Niste dobro potvrdili sifru."));
			return ok(changePassword.render(u));
		}
		password = HashHelper.createPassword(password);
		u.setPassword(password);
		u.save();
		Logger.of("user").info("User "+ u.username + " changed their password successfully");
		flash("chng_pass_success", Messages.get("Uspjesno ste zamijenili vasu sifru."));
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
	 * If user is administrator send him to adminProfile.html
	 * @return adminProfile.html
	 */
	@Security.Authenticated(AdminFilter.class)
    public static Result adminPanel() {
		List<Product> specialProducts = ProductController.findProduct.where().eq("isSold", false).eq("isSpecial", true).findList();
		List<Product> products = ProductController.findProduct.where().eq("isRefunding", true).findList();
   	  	usernameSes = session(SESSION_USERNAME);
   	  	User u = User.finder(usernameSes);
   	 return ok(adminPanel.render(specialProducts, u, products));
    }
	
	/**
	* Upload image for User profile, and show picture on user /profile.html. 
	* If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
	* If file size is bigger then 2MB, redirect user on profile without uploading image.
	* @return
	*/
	public static Result saveFile(){
		User u = SessionHelper.getCurrentUser(ctx());
		usernameSes = session(SESSION_USERNAME);
		
		//checks if picture exists in base, if it does deletes it then uploads new picture
		final String deletePath = "." + File.separator 
				+ "public" + File.separator;
		String s = findUser.byId(u.id).imagePath;
		String defaultPic = "images" + File.separator + "profilePicture" + File.separator + "profileimg.png";
		
		if (s != null && !s.equals(defaultPic)){
			File d = new File(deletePath + s);
			d.delete();
		}
			
		int userID = User.finder(usernameSes).id;
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
			
			flash("error",  Messages.get("Slika ne smije biti veca od 2 MB"));
			Logger.of("user").warn( usernameSes + " tried to upload an image that is not valid.");
			return redirect("/profile");
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){
			flash("error",  Messages.get("Image size not valid"));
			Logger.of("user").warn( usernameSes + " tried to upload an image that is bigger than 2MB.");
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
			Logger.of("user").error( usernameSes + " failed to upload an image to his profile page.");
			e.printStackTrace();
		}
		flash("upload_img_success",  Messages.get("Uspjesno ste objavili sliku"));
		return redirect("/profile");
	}
	
	/**
	 * Method finds one product and sends
	 * it to view.
	 * @param id
	 */
	
	public static Result showPurchase(int id)
	{
		return ok(purchase.render(id));
	}
	
	/**
	 * Method integrates PayPal with application, using access token.
	 * It adds amount, currency, payer, description, intent and state
	 * for payment. 
	 * @param id
	 * @return
	 */

	public static Result purchaseProcessing(int id)

	{
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

			amount.setTotal("" + p.price);
			amount.setCurrency("EUR");
			
			

			// We put the amount in USD and convert it to a String;
			amount.setTotal(p.getPriceinStringinUSD());
			amount.setCurrency("USD");
			Transaction transaction = new Transaction();
			transaction.setDescription("Cestitamo, jos ste samo nekoliko koraka od kupovine proizvoda '" + p.name +
										"' sa slijedecim opisom : '" + p.description + "'");

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
			
			accessToken = new OAuthTokenCredential(payPalSecretKey1, payPalSecretKey2).getAccessToken();
			APIContext apiContext = new APIContext(accessToken);
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
		usernameSes = session(SESSION_USERNAME);
		if(u == null)
		{
			return redirect(routes.Application.index());
		}
		User receiver = User.find(id);
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
		User sender = SessionHelper.getCurrentUser(ctx());
		usernameSes = session(SESSION_USERNAME);
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
   	  	return redirect("/allMessages");
	}
	
	/**
	 * Method sends two lists to view, list of received messages
	 * and list of sent messages for one user.
	 */
	public static Result allMessages()
	{
		User u = SessionHelper.getCurrentUser(ctx());
		usernameSes = session(SESSION_USERNAME);
		if(u == null)
	  	{
	  		return redirect(routes.Application.index());
	  	}
		List<PrivateMessage> messagesReceived = PrivateMessage.find.where().eq("receiver.id", u.id).findList();
		List<PrivateMessage> messagesSent = PrivateMessage.find.where().eq("sender.id", u.id).findList();
		
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
		return redirect("http://localhost:9000/buyingAProduct/" +id +"/"+ token);
	}	

}
