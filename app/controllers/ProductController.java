package controllers;

import helpers.SessionHelper;

import java.util.List;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import models.MainCategory;
import models.Product;
import models.SubCategory;
import models.User;
import views.html.*;
import play.Logger;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.i18n.Messages;

import com.google.common.io.Files;


public class ProductController extends Controller {

	static Form<Product> newProduct = new Form<Product>(Product.class);
	static Finder<Integer, Product> findProduct = new Finder<Integer, Product>(
			Integer.class, Product.class);
	static String usernameSes;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result showProduct(int id) {
		User u = helpers.SessionHelper.getCurrentUser(ctx());
		Product p = ProductController.findProduct.byId(id);
		return ok(showProduct.render(p, u));
	}

	/**
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product needs
	 * to be filled in order to add the Product;
	 */
	public static Result addProduct() {
		usernameSes = session("username");
		User currentUser=SessionHelper.getCurrentUser(ctx());
		// 1. Ako nije registrovan da mu oneomogucimo prikaz addProduct.html;
		if (usernameSes == null) {
			return redirect(routes.Application.index());
		}
		// 2. Zabrana admin user-u da objavljuje proizvod;
		if (currentUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		List<MainCategory> mainCategoryList = MainCategory.find.all();
		return ok(addProduct.render(usernameSes, mainCategoryList));

	}

	/**
	 * From the filled form takes the necessary values; And creates the object
	 * Product using the constructor; Sends the information about the product
	 * created to the showProduct.html page; Method creates(saves) the product
	 * using the create method from the Product model;
	 * 
	 * @return showProducts.html with the necessary variables;
	 */
	public static Result createProduct() {
		usernameSes = session("username");
		String name = newProduct.bindFromRequest().get().name;
		String desc = newProduct.bindFromRequest().get().description;
		Double price = newProduct.bindFromRequest().get().price;
		String mainCategory = newProduct.bindFromRequest().get().categoryString;
		String subCategory = newProduct.bindFromRequest().get().subCategoryString;
		String availability = newProduct.bindFromRequest().get().availability;

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
		String productImagePath = newProduct.bindFromRequest().get().productImagePath;

		User u = User.finder(usernameSes);
		Product p = Product.create(name, desc, price, u, mc, sc, availability, productImagePath);
		Logger.of("product").info("User "+ usernameSes +" added a new product '" + p.name + "'");
		return redirect("/addPictureProduct/" + p.id);
	}

	/**
	 * Finds the product under the id number; And sends that product to the
	 * editProduct.html page on redering; Where we will have a new form that we
	 * will edit;
	 * 
	 * @param id
	 * @return
	 */
	public static Result editProduct(int id) {
		 User currentUser=SessionHelper.getCurrentUser(ctx());
	   	 usernameSes = session("username");
	   	 Product p = findProduct.byId(id);
	   	 if(p == null) {
	 		Logger.of("product").warn("Failed try to update a product which doesnt exist");
	   		return redirect(routes.Application.index());
	   	 }
	   	 List<MainCategory> mainCategoryList = MainCategory.find.all();
	    //  Ako nije registrovan da mu onemogucimo prikaz editProduct.html;
	   			 if (usernameSes == null) {
	   				Logger.of("product").warn("Not registered user tried to update a product");
	   				 return redirect("/");
	   			 }
	   			 
	   			 if(!currentUser.username.equals(p.owner.username)) {
	   				Logger.of("product").warn(usernameSes + " tried to update an anothers user's product");
	   				return redirect(routes.Application.index());
	   			 }
	   	 //  Ako je admin ulogovan, onemogucujemo mu da edituje proizvod;
	   			 if (currentUser.isAdmin==true) {
	   				Logger.of("product").warn("An admin tried to update a users product");
	   				return redirect(routes.Application.index());
	   			 }
	   	 //  Prosle sve provjere, tj. dozvoljavamo samo registrovanom useru <svog proizvoda> da ga edituje;    
	   	 return ok(editProduct.render(usernameSes, p, mainCategoryList));
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
		usernameSes = session("username");
		String name = newProduct.bindFromRequest().get().name;
		String desc = newProduct.bindFromRequest().get().description;
		Double price = newProduct.bindFromRequest().get().price;
		String mainCategory = newProduct.bindFromRequest().get().categoryString;
		String subCategory = newProduct.bindFromRequest().get().subCategoryString;
		String availability = newProduct.bindFromRequest().get().availability;

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
		p.setPrice(price);
		p.setCategory(mc);
		p.setSubCategory(sc);
		p.setAvailability(availability);
		p.save();
		Logger.of("product").info("User "+ usernameSes + " updated the info of product " + oldname + ", NAME : ["+p.name+"]");
		oldname = null;
		return redirect("/showProduct/" + id);	
	}

	/**
	 * Deletes the products found under the given id;
	 * 
	 * @param id
	 * @return
	 */
	public static Result deleteProduct(int id) {
		String toBeDeleted = Product.find.byId(id).name;
		Product.delete(id);
		Logger.of("product").info( session("username") + " deleted the product "+toBeDeleted);
		toBeDeleted = null;
		return redirect(routes.UserController.findProfileProducts());
	}	
	
	/**
	 * @param id is Product id
	 * @return redirect to html for adding picture
	 */
	public static Result productPicture(int id) {
		   	  	usernameSes = session("username");
			   	 Product p = findProduct.byId(id);
		
		   	 return ok(addPictureProduct.render(usernameSes, p));
		    }
	
	
	
	/**
	 * Uplade image for User profile, and show picture on user /profile.html. 
	 * If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
	 * If file size is bigger then 2MB, redirect user on profile without uploading image.
	 * @return
	 */
	
	public static Result saveFile(int id){
	
		User u = SessionHelper.getCurrentUser(ctx());
		usernameSes = session("username");
		
	   	Product p = findProduct.byId(id);
	   	 		
   	  	//creating path where we are going to save image
		final String savePath = "." + File.separator 
				+ "public" + File.separator + "images" 
				+ File.separator + "productPicture" + File.separator;
		
		//it takes uploaded information  
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("image");
		File image = filePart.getFile();
		//it takes extension from image that is uploaded
		String extension = filePart.getFilename().substring(filePart.getFilename().lastIndexOf('.'));
		extension.trim();
		
		//If file is not image format jpg, jpeg or png redirect user on profile without uploading image.
		if(	   !extension.equalsIgnoreCase(".jpeg") 
			&& !extension.equalsIgnoreCase(".jpg")
			&& !extension.equalsIgnoreCase(".png") ){
		
			flash("error",  Messages.get("Image type not valid"));
			Logger.of("product").warn( usernameSes + " tried to upload an image that is not valid.");
			return redirect("/showProduct");
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){
			flash("error",  Messages.get("Image size not valid"));
			Logger.of("product").warn( usernameSes + " tried to upload an image that is bigger than 2MB.");
			return redirect("/showProduct");
		}
		
		//creating image name from user id, and take image extension, than move image to new location
		try {
			File profile = new File(savePath + UUID.randomUUID().toString() + extension);
			Files.move(image, profile );		
			String assetsPath = "images" 
					+ File.separator + "productPicture" + File.separator + profile.getName();
			p.productImagePath = assetsPath;
			p.save();
		} catch (IOException e) {
			Logger.of("product").error( usernameSes + " failed to upload an image to the product " +p.name);
			e.printStackTrace();
		}
		flash("successAddProduct", Messages.get("Uspjesno ste objavili oglas"));
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
			usernameSes = session("username");
			Product p = findProduct.byId(id);
			p.productImagePath = null;
			p.save();
			flash("successAddProduct", Messages.get("Uspjesno ste objavili oglas"));
			return redirect("/showProduct/"+p.id);
	}
}
