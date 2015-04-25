package controllers;

import helpers.JsonHelper;
import helpers.MailHelper;
import helpers.SessionHelper;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import models.Blogger;
import models.Comment;
import models.ImgPath;
import models.MainCategory;
import models.Newsletter;
import models.Product;
import models.SubCategory;
import models.TransactionP;
import models.User;
import models.Statistics;
import views.html.*;
import play.Logger;
import play.Play;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.i18n.Messages;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.io.Files;


public class ProductController extends Controller {
	
	/**
	 * Inner Class FilteredSearch
	 *Used for the purposes of the Form of the filtered Search;
	 */
	public static class FilteredSearch{
		public String priceMin;
		public String priceMax;
		public String desc;
		public String availabilityS;
		
		/**
		 * Constructor;
		 */
		public FilteredSearch(){
					
		}
		
		/**
		 * Constructor with parameters;
		 * @param priceMin
		 * @param priceMax
		 * @param availability
		 */
		public FilteredSearch(String priceMin,String priceMax,String availability){
			this.priceMin = priceMin;
			this.priceMax = priceMax;			
		}
	}

	static Form<Product> newProduct = new Form<Product>(Product.class);
	static Form<TransactionP> newTransaction = new Form<TransactionP>(TransactionP.class);
	static Form<FilteredSearch> filteredSearch=new Form<FilteredSearch>(FilteredSearch.class);
	public static Finder<Integer, Product> findProduct = new Finder<Integer, Product>(Integer.class, Product.class);
	static Form<Comment> postComment = new Form<Comment>(Comment.class);
	static String usernameSes;
	public static final String OURHOST = Play.application().configuration().getString("OURHOST");


	/**
	 * Method showProduct() renders the showProduct page with the product that
	 * has been clicked on;
	 * 
	 * @param id
	 * @return Renders the showProduct page;
	 */
	public static Result showProduct(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Product p = ProductController.findProduct.byId(id);
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		List<Comment> commentList = Comment.find.all();
		// We set the number of views for this item to +1 more;
		p.statsProducts.setNoOfClicks(p.statsProducts.getNoOfClicks() + 1);
		p.statsProducts.save();
		p.save();
		if (!request().accepts("text/html")) {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			array.add(JsonHelper.jsonProduct(p));
			array.add(JsonHelper.jsonUser(u));
			array.add(JsonHelper.jsonCategoryList(mainCategoryList));
			array.add(JsonHelper.jsonCommentList(commentList));
			return ok(array);
		}
		return ok(showProduct.render(p, u, mainCategoryList, commentList));
	}
	
	/**
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product needs
	 * to be filled in order to add the Product;
	 * 
	 * @return renders the addProduct page;
	 */
	public static Result addProduct() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// Unregistred user check
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		// Admin can not create product
		if (currentUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		if (!request().accepts("text/html")) {
			return JsonController.addProduct();
		}
		return ok(addProduct.render(mainCategoryList));
	}

	/**
	 * From the filled form and input fields takes the necessary values; And
	 * creates the object Product using the constructor; Sends the information
	 * about the product created to the showProduct.html page; Method
	 * creates(saves) the product using the create method from the Product
	 * model;
	 * 
	 * @return showProducts.html with the necessary variables;
	 */
	public static Result createProduct() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
		String name;
		String desc;
		String longDesc;
		Double price;
		String mainCategory;
		String subCategory;
		String location;
		String condition;
		String exchange;
		
		try {
			name = newProduct.bindFromRequest().get().name;
			desc = newProduct.bindFromRequest().get().description;
			longDesc = newProduct.bindFromRequest().get().longDescription;
			price = newProduct.bindFromRequest().get().price;
			mainCategory = newProduct.bindFromRequest().get().categoryString;
			subCategory = newProduct.bindFromRequest().get().subCategoryString;
			location = newProduct.bindFromRequest().get().location;
			condition=newProduct.bindFromRequest().get().condition;
			exchange=newProduct.bindFromRequest().get().exchange;
			
		} catch(IllegalStateException e) {
			flash("add_product_null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
			return redirect(routes.ProductController.addProduct());
		}
		MainCategory mc = MainCategory.findMainCategoryByName(mainCategory);
		List<SubCategory> subCats = mc.subCategories;
		Iterator<SubCategory> iter = subCats.iterator();
		SubCategory sc = null;
		while (iter.hasNext()) {
			SubCategory temp = iter.next();
			if (temp.name.equals(subCategory)) {
				sc = temp;
				break;
			}
		}
		Product p = Product.create(name, desc, longDesc, price, u, mc, sc, location,condition,exchange);
		Logger.of("product").info("User "+ u.username +" added a new product '" + p.name + "'");
		return redirect("/addPictureProduct/" + p.id);
	}

	/**
	 * Finds the product under the id number; And sends that product to the
	 * editProduct.html page on redering; Where we will have a new form that we
	 * will edit;
	 * 
	 * @param id
	 * @return Renders the editProduct page with the parameters product found by
	 *         the id and the category list;
	 */
	public static Result editProduct(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		 User currentUser = SessionHelper.getCurrentUser(ctx());
	   	 Product p = findProduct.byId(id);
	   	 
	   	 if (p == null) {
	 		Logger.of("product").warn("Failed try to update a product which doesnt exist");
	   		return redirect(routes.Application.index());
	   	 }
	   	 
	   	 List<MainCategory> mainCategoryList = MainCategory.find.all();
	   	 
	   	 //  Ako nije registrovan da mu onemogucimo prikaz editProduct.html;
	   	 if (currentUser == null) {
	   		Logger.of("product").warn("Not registered user tried to update a product");
	   		return redirect(routes.Application.index());
		}
	   			 
	   	if(!currentUser.username.equals(p.owner.username)) {
	   		Logger.of("product").warn(currentUser.username + " tried to update an anothers user's product");
  			return redirect(routes.Application.index());
 		}
	   	//  Ako je admin ulogovan, onemogucujemo mu da edituje proizvod;
	   	if (currentUser.isAdmin==true) {
			Logger.of("product").warn("An admin tried to update a users product");
 			return redirect(routes.Application.index());
   		}
	   	if (!request().accepts("text/html")) {
	   		return JsonController.editProduct(id);
	   	}
   		//  Prosle sve provjere, tj. dozvoljavamo samo registrovanom useru <svog proizvoda> da ga edituje;    
   		return ok(editProduct.render(p, mainCategoryList));
    }

	/**
	* Saves the new values of the attributes that are entered and overwrites
	* over the ones that were entered before;
	* 
	* @param id
	* @return redirect("/showProduct/" + id);
	*/
	public static Result saveEditedProduct(int id) {
		// takes the new attributes that are entered in the form;
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		String name;
		String desc;
		String longDesc;
		Double price;
		String mainCategory;
		String subCategory;
		String location;
		String condition;
		String exchange;
		
		try {
			name = newProduct.bindFromRequest().get().name;
			desc = newProduct.bindFromRequest().get().description;
			longDesc = newProduct.bindFromRequest().get().longDescription;
			price = newProduct.bindFromRequest().get().price;
			mainCategory = newProduct.bindFromRequest().get().categoryString;
			subCategory = newProduct.bindFromRequest().get().subCategoryString;
			location = newProduct.bindFromRequest().get().location;
			condition=newProduct.bindFromRequest().get().condition;
			exchange=newProduct.bindFromRequest().get().exchange;
		} catch(IllegalStateException e) {		
			flash("edit_product_null_field", Messages.get("Molim Vas popunite sva polja u formi."));
			return redirect(routes.ProductController.editProduct(id));
		}

		MainCategory mc = MainCategory.findMainCategoryByName(mainCategory);
		List<SubCategory> subCats = mc.subCategories;
		Iterator<SubCategory> iter = subCats.iterator();
		SubCategory sc = null;
		while (iter.hasNext()) {
			SubCategory temp = iter.next();
			if (temp.name.equals(subCategory)) {
				sc = temp;
				break;
			}
		}

		// sets all the new entered attributes as the original ones from the
		// product;
		// and saves();
		Product p = findProduct.byId(id);
		String oldname = p.name;
		p.setName(name);
		p.setDesc(desc);
		p.setLongDescription(longDesc);
		p.setPrice(price);
		p.setCategory(mc);
		p.setSubCategory(sc);
		p.setLocation(location);
		p.setCondition(condition);
		p.setExchange(exchange);
		p.save();
		 
		Logger.of("product").info("User "+ u.username + " updated the info of product " + oldname + ", NAME : ["+p.name+"]");
		oldname = null;
		flash("edit_product_success", Messages.get("Uspješno ste izmijenili oglas"));
		return redirect("/showProduct/" + id);	
	}

	/**
	 * Deletes the products found under the given id;
	 * 
	 * @param id
	 * @return redirects the User to the profile page;
	 */
	public static Result deleteProduct(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if (u != null && u.username.equals("blogger")) {
			return ok(blog.render(bloggerList, u));
		}
		deletePicture(id);
		String toBeDeleted = Product.find.byId(id).name;
		Product.delete(id);
		Logger.of("product").info( session("username") + " deleted the product " + toBeDeleted);
		toBeDeleted = null;
		flash("delete_product_success",  Messages.get("Uspješno ste izbrisali oglas"));
		return redirect(routes.UserController.findProfileProducts());
	}	
	
	/**
	 * The method is used when deleting product; In order for this method to
	 * delete the picture of the product if a user is deleting the product
	 * itself;
	 * 
	 * @param id
	 */
	public static void deletePicture(int id){
		
		final String deletePath = "." + File.separator 
				+ "public" + File.separator;
		String defaultPic = "images" + File.separator + "productPicture" + File.separator + "no-img.jpg";
				
		List<ImgPath> imgList= findProduct.byId(id).imgPathList;
		
		for (int i = 0; i< imgList.size() ; i++){
			String s = imgList.get(i).imgPath;
			
			if (!s.equals(defaultPic)) {
				File file = new File(deletePath + s);
				file.delete();
			}				
		}			
	}
	
	public static Result deleteOnePicture(int id, String imgPath){
		
//		final String deletePath = "." + File.separator 
//				+ "public" + File.separator;
//		
//		Product p = findProduct.byId(id);
//		List<ImgPath> imgList= p.imgPathList;
//		
//		if (imgList.size() == 1) {
//			p.imgPathList.add()
//		}
//		
//		for (int i = 0; i< imgList.size() ; i++){
//			String s = imgList.get(i).imgPath;
//			
//			if (!s.equals("images/no-img.jpg") && imgPath.equals(s)){
//				File file = new File(deletePath + s);
//				file.delete();
//				
//				
//			}
//						
//		}
		
		return redirect("/showProduct/"+ id);
	}
	
	/**
	 * @param id is Product id
	 * @return redirect to html for adding picture
	 */
	public static Result productPicture(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Product p = findProduct.byId(id);
		return ok(addPictureProduct.render(p));
	}
		
	/**
	 * Upload image for User profile, and show picture on user /profile.html. 
	 * If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
	 * If file size is bigger then 2MB, redirect user on profile without uploading image.
	 * @return
	 */
	
	public static Result saveFile(int id){
		
		
	   	Product p = findProduct.byId(id);
	   	User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
	   	 			
   	  	//creating path where we are going to save image
		final String savePath = "." + File.separator 
				+ "public" + File.separator + "images" 
				+ File.separator + "productPicture" + File.separator;
		
		//it takes uploaded information  
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("image");
		if (filePart == null){
			 flash("error",  Messages.get("Niste uploadovali sliku"));
			 return redirect("/addPictureProduct/" + id);
		}
		File image = filePart.getFile();
		
		//it takes extension from image that is uploaded
		String extension = filePart.getFilename().substring(filePart.getFilename().lastIndexOf('.'));
		extension.trim();
		
		//If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
		if(	   !extension.equalsIgnoreCase(".jpeg") 
			&& !extension.equalsIgnoreCase(".jpg")
			&& !extension.equalsIgnoreCase(".png") ){
		
			flash("error",  Messages.get("Niste unijeli sliku"));
			Logger.of("product").warn( u.username + " tried to upload an image that is not valid.");
			return redirect("/addPictureProduct/" + id);
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){

			flash("error",  Messages.get("Slika ne smije biti veca od 2 MB"));
			Logger.of("product").warn( u.username + " tried to upload an image that is bigger than 2MB.");

			flash("error",  Messages.get("Slika ne smije biti veća od 2 MB"));
			Logger.of("product").warn( u.username + " tried to upload an image that is bigger than 2MB.");

			return redirect("/addPictureProduct/" + id);
		}
		
		//creating image name from user id, and take image extension, than move image to new location
		try {
			File profile = new File(savePath + UUID.randomUUID().toString() + extension);
			Files.move(image, profile );		
			String assetsPath = "images" 
					+ File.separator + "productPicture" + File.separator + profile.getName();
			p.productImagePath = assetsPath;
			ImgPath imp = new ImgPath(assetsPath, p);
			p.imgPathList.add(imp);
			p.save();
		} catch (IOException e) {
			Logger.of("product").error( u.username + " failed to upload an image to the product " +p.name);
			e.printStackTrace();
		}
		

		flash("add_product_success", Messages.get("Uspjesno ste uploadali sliku"));

		flash("add_product_success", Messages.get("Uspješno ste objavili oglas"));


		return redirect("/showProduct/"+p.id);
	}
	
	/**
	 * If the user does not want to upload a picture.
	 * If we wants to publish the product with no picture.
	 * @param id
	 * @return Result redirect("/showProduct/"+p.id);
	 */
	public static Result saveNoFile(int id){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
			Product p = findProduct.byId(id);
			flash("add_product_success", Messages.get("Uspješno ste objavili oglas"));
			return redirect("/showProduct/"+p.id);
	}

	/**
	 * When a product is bought, the items attribute boolean isSold is set to true;
	 * and the buyerUser is set to the user who is currently logged in, that is, set
	 * to the user who has clicked (later gone through the procedure of the PayPal 
	 * process);
	 * @param product_id
	 * @return
	 */
	public static Result buyProductSuccess(int product_id, String token) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User buyerUser = SessionHelper.getCurrentUser(ctx());
		//1. No permission for unregistered user;
		if (buyerUser == null) {
			return redirect(routes.Application.index());
		}
		//2. No permission for an admin user;
		if (buyerUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		Product p = findProduct.byId(product_id);
		//3. URL Security - No Product under the given id number;
		if (p == null) {
			return redirect(routes.Application.index());
		}
		// URL Security;
		//4. No permission for the user to buy his own product (BE Security);
		// Although we will hide the "KUPI"/"BUY" button from the user for
		// his own products on certain .html pages; with listing of products;
				
		if (buyerUser == p.owner) {
			return redirect(routes.Application.index());
		}
		TransactionP temp = new TransactionP(token, p);
		p.setPurchaseTransaction(temp);
		p.setSold(true);
		p.setBuyerUser(buyerUser);
		buyerUser.bought_products.add(p);
		p.save();
		List <Product> l = ProductController.findProduct.where().eq("owner.username", buyerUser.username).eq("isSold", false).findList();
		Logger.of("product").info("User "+ buyerUser.username +" bought the product '" + p.name + "'");
		flash("buy_product_success", Messages.get("Čestitamo, uspješno ste kupili proizvod. Proizvod pogledajte pod Kupljeni proizvodi!"));
		MailHelper.sendNewsletterMessage(p.owner.email, "Čestitamo, uspješno ste prodali proizvod " + p.name + ", za " + p.price + " KM");
		MailHelper.sendNewsletterMessage(buyerUser.email, "Čestitamo, uspješno ste kupili proizvod " + p.name + ", za " + p.price + " KM");
		return ok(profile.render(l, buyerUser));
	}

	
	/**
	 * When a paypal procedure has failed for some reason (creditcard number wrong or any kind of error occured in the
	 * process), we redirect the user to his profile page, with the list of the products if he has any.
	 * @param product_id
	 * @return we render the .html page : profile.render(l, buyerUser));
	 *//*
	public static Result buy_product_fail(int product_id) {
		Product p = findProduct.byId(product_id);
		User buyerUser = SessionHelper.getCurrentUser(ctx());
		List <Product> l = ProductController.findProduct.where().eq("owner.username", buyer_user.username).eq("isSold", false).findList();
		Logger.of("product").info("User "+ buyerUser.username +" failed to buy the product '" + p.name + "'");
		return ok(profile.render(l, buyerUser));
	}
	*/

	/**
	 * 
	 * @param q
	 * @return
	 */
	public static Result searchUsers(String q){

		User currentUser = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(currentUser != null && currentUser.username.equals("blogger")){
			return ok(blog.render(bloggerList,currentUser));
		}

		List<Product>products=Product.find.where("(UPPER(name) LIKE UPPER('%"+q+"%')) AND ((isSold) LIKE (false)) AND (isSpecial LIKE ('false'))").findList();
		Logger.error("String(Product controller): " + q);
		if(currentUser!=null){
			Date currentDate = new Date();
			Newsletter newsletter =new Newsletter(q, currentDate, currentUser);
			newsletter.save();
			currentUser.newsletter.add(newsletter);
			currentUser.save();
		}
		
		List<Product>sproducts=Product.find.where("(UPPER(name) LIKE UPPER('%"+q+"%')) AND ((isSold) LIKE (false)) AND (isSpecial LIKE ('true'))").findList();
		List<User>users=User.findInt.where("UPPER(username) LIKE UPPER('%"+q+"%')").findList();
		return ok(listaPretrage.render(sproducts,products,users));	
	}
	
	/**
	 * Method refundProduct allows User to send product he purchased to the
	 * administration in order they can review it and refund the buyer
	 * @param id
	 * @return
	 */
	public static Result refundProduct(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		String refundReason;
		try {
			refundReason = newProduct.bindFromRequest().get().refundReason;
		} catch(IllegalStateException e) {
			return redirect("/showProduct/" + id);
		}
		
		if (refundReason == null) {
			return redirect(routes.Application.index());
		}
		
		Product p = Product.find.byId(id);
		p.isRefunding = true;
		p.refundReason = refundReason;
		p.save();
		
		//Email sending
		User seller = p.owner;
		User buyer = p.buyerUser;
		
		MailHelper.sendRefundEmail(buyer.email, seller.email, OURHOST + "/showProduct/" + id);
		

		return redirect("/showProduct/" + id);
	}
	
	
	/**
	 * Method denyRefund allows Administrator to deny refund asked by buyer and
	 * sets it refunding fields to false
	 * @param id
	 * @return
	 */
	public static Result denyRefund(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Product p = Product.find.byId(id);
		p.isRefunding = false;
		p.refundable = false;
		p.save();
		
		//Email sending
		User seller = p.owner;
		User buyer = p.buyerUser;
		
		MailHelper.sendRefundEmailDenial(buyer.email, seller.email, OURHOST + "/showProduct/" + id);
		
		return redirect("/showProduct/" + id);
		
	}
	
	/**
	 * This method leaveCommentTransaction, checks which user is currently logged in,
	 * As if the buyer is logged in, the method takes the comment from the form and sets it 
	 * as the comment of the buyer to the transaction of the product that he has
	 * bought.
	 * @param id
	 * @return
	 */
	public static Result leaveBCommentTransaction(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// If no User is logged in;
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
	
		String comment;
		// In the case, when the Buyer, leaves a comment to the transaction;
				try {
					comment = newTransaction.bindFromRequest().get().buyer_comment;
				} catch(IllegalStateException e) {
					return redirect("/showProduct/" + id);
				}
				//2. Second check;
				if (comment == null) {
					return redirect(routes.Application.index());
					}
				Product p = Product.find.byId(id);
				p.purchaseTransaction.setBuyer_comment(comment);
				p.save();
				return redirect("/showProduct/" + id);
	}
	
	/**
	 * This method leaveCommentTransaction, checks which user is currently logged in,
	 * As if the seller is logged in, the method takes the comment from the form and sets it
	 * as the comment of the seller (x-owner) to the transaction of the products that he 
	 * has sold.
	 * @param id
	 * @return
	 */
	public static Result leaveSCommentTransaction(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// If no User is logged in;
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
	
		String comment;
		// In the case, when the Seller, leaves a comment to the transaction;
				try {
					comment = newTransaction.bindFromRequest().get().seller_comment;
				} catch(IllegalStateException e) {
					return redirect("/showProduct/" + id);
				}
				//2. Second check;
				if (comment == null) {
					return redirect(routes.Application.index());
					}
				Product p = Product.find.byId(id);
				p.purchaseTransaction.setSeller_comment(comment);
				p.save();
				return redirect("/showProduct/" + id);
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public static Result filteredSearch(String ids1,String ids2){
		Logger.debug(ids1);
		Logger.debug(ids2);
		String[] productsIDs1 = ids1.split(",");		
		List<Product>products = new ArrayList<Product>();
		String temp;
		if(!ids1.isEmpty()){
		for(String id: productsIDs1){
			int currentID = Integer.parseInt(id);
			temp=""+currentID;
			Logger.debug(temp);
			Product currentProduct = Product.find.byId(currentID);			
			products.add(currentProduct);
		}
		}
		
		List<Product>sproducts = new ArrayList<Product>();
		if(!ids2.isEmpty()){
		String[] productsIDs2 = ids2.split(",");		
		for(String id: productsIDs2){
			int scurrentID = Integer.parseInt(id);
			temp=""+scurrentID;
			Logger.debug(temp);
			Product scurrentProduct = Product.find.byId(scurrentID);			
			sproducts.add(scurrentProduct);
		}
		}
		
		if((products.isEmpty())&(sproducts.isEmpty())){
			Logger.info("No searched products or special products");
			return ok(newViewForFilter.render(sproducts,products,MainCategory.allMainCategories()));
		}
		else if(products.isEmpty()){
			Logger.info("No searched products");
		}
		else{
			Logger.info("No searched special products");
		}
		
		List<Product>productList=new ArrayList<Product>();
		List<Product>sproductList=new ArrayList<Product>();
	    double priceMin = 0;
	    double priceMax = 999999999;
		String availability ;
		String descr;
		if(filteredSearch.hasErrors()){
			Logger.info("Error in form");
			return ok(newViewForFilter.render(sproducts,products,MainCategory.allMainCategories()));
			}

		String min=filteredSearch.bindFromRequest().get().priceMin;
		String max=filteredSearch.bindFromRequest().get().priceMax;
		descr=filteredSearch.bindFromRequest().get().desc;
		availability=filteredSearch.bindFromRequest().get().availabilityS;
		if(!min.isEmpty()){
			priceMin=Double.parseDouble(min);
		}
		if(!max.isEmpty()){
			priceMax=Double.parseDouble(max);
		}
		if(descr.isEmpty()){
			productList=Product.find.where("(location LIKE '"+availability+"') AND ((price>="+priceMin+" AND price<="+priceMax+")) AND (isSold LIKE ('false')) AND (isSpecial LIKE ('false'))").findList();
			sproductList=Product.find.where("(location LIKE '"+availability+"') AND ((price>="+priceMin+" AND price<="+priceMax+")) AND (isSold LIKE ('false')) AND (isSpecial LIKE ('true'))").findList();
	    }else{

		productList=Product.find.where("(location LIKE '"+availability+"') AND ((price>="+priceMin+" AND price<="+priceMax+")) AND UPPER(description) LIKE UPPER('%"+descr+"') AND (isSold LIKE ('false')) AND (isSpecial LIKE ('false'))").findList();
		sproductList=Product.find.where("(location LIKE '"+availability+"') AND ((price>="+priceMin+" AND price<="+priceMax+")) AND UPPER(description) LIKE UPPER('%"+descr+"') AND (isSold LIKE ('false')) AND (isSpecial LIKE ('true'))").findList();
		 }
		
		List<Product>filteredProducts=new ArrayList<Product>();
		for(Product product: products){
			if(productList.contains(product)){
				filteredProducts.add(product);
			}
		}
		
		List<Product>sfilteredProducts=new ArrayList<Product>();
		for(Product product: sproducts){
			if(sproductList.contains(product)){
				sfilteredProducts.add(product);
			}
		}
		return ok(newViewForFilter.render(sfilteredProducts,filteredProducts,MainCategory.allMainCategories()));
	}
}
