package controllers;

import java.util.List;
<<<<<<< HEAD
import java.util.Iterator;
=======
import java.io.File;
import java.io.IOException;
>>>>>>> imageProductDev

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
		usernameSes = session("username");
		// 1. Ako nije registrovan da omogucimo prikaz proizvoda;
		if (usernameSes == null) {
			usernameSes = "";
		}
		Product p = ProductController.findProduct.byId(id);
		return ok(showProduct.render(usernameSes, p));
	}

	/**
	 * Method takes the usernameSes from the session variable and sends it to
	 * the addProduct.html page; Where the Form for publishing the product needs
	 * to be filled in order to add the Product;
	 */
	public static Result addProduct() {
		usernameSes = session("username");
		// 1. Ako nije registrovan da mu oneomogucimo prikaz addProduct.html;
		if (usernameSes == null) {
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
		Logger.of("product").info("User "+ usernameSes +" added a new product");

		return redirect("/showProduct/" + p.id);	
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
	   			 
	   			 if(!currentUser.getUsername().equals(p.owner.getUsername())) {
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
		p.setName(name);
		p.setDesc(desc);
		p.setPrice(price);
		p.setCategory(mc);
		p.setSubCategory(sc);
		p.setAvailability(availability);
		p.save();

		Logger.of("product").info("User "+ usernameSes + " updated a product");
		return redirect("/showProduct/" + id);	
	}

	/**
	 * Deletes the products found under the given id;
	 * 
	 * @param id
	 * @return
	 */
	public static Result deleteProduct(int id) {

		  Product.delete(id);
		  Logger.of("product").info( session("username") + " deleted a product");
		  return redirect(routes.UserController.findProfileProducts());
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
	   	 int idProduct = p.id;
		
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
			
			flash("error", "Image type not valid");
			return redirect("/showProduct");
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){
			flash("error", "Image size not valid");
			return redirect("/showProduct");
		}
		
		//creating image name from user id, and take image extension, than move image to new location
		try {
			File profile = new File(savePath + idProduct + extension);
			Files.move(image, profile );		
			String assetsPath = "images" 
					+ File.separator + "productPicture" + File.separator + profile.getName();
			p.productImagePath = assetsPath;
			p.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redirect("/showProduct/"+p.id);
	}
	
	
}
