package controllers;

import helpers.JsonHelper;
import helpers.SessionHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.io.Files;

import models.Blogger;
import models.Comment;
import models.ImgPath;
import models.MainCategory;
import models.Product;
import models.User;
import play.Logger;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.*;
import views.html.*;



public class BloggerController extends Controller{
	
	
	static Form<Blogger> newBlogger = new Form<Blogger>(Blogger.class);
	static Finder<Integer, Blogger> findBlogger = new Finder<Integer, Blogger>(Integer.class, Blogger.class);

	
		
	
	public static Result showBlog() {
		User u = helpers.SessionHelper.getCurrentUser(ctx());
		
		List<Blogger> bloggerList = Blogger.find.all();
		
		Blogger[] array = new Blogger[bloggerList.size()];
		int count = bloggerList.size()-1;
		Iterator<Blogger> iter = bloggerList.iterator();
		while (iter.hasNext()) {
			
			array[count] = iter.next();
			count--;
			
		}
		List<Blogger> correct = Arrays.asList(array);
		
		
		
		return ok(blog.render(correct,u));
	}
	
	public static Result showOneBlog(int id) {
User u = helpers.SessionHelper.getCurrentUser(ctx());
		
		List<Blogger> bloggerList = Blogger.find.all();
		
		Blogger[] array = new Blogger[bloggerList.size()];
		int count = bloggerList.size()-1;
		Iterator<Blogger> iter = bloggerList.iterator();
		while (iter.hasNext()) {
			//Blogger current = 
			array[count] = iter.next();
			count--;
		}
		List<Blogger> correct = Arrays.asList(array);
		
		Blogger b = findBlogger.byId(id);
		
		
		
		return ok(oneBlog.render(b,u,correct));
	}
	
	
	public static Result addBlog(){
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// Unregistred user check
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		// Admin can not create blog
		if (currentUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		return ok(createBlog.render());
	}
	
	public static Result createBlog(){
		String name;
		String desc;
		String longDesc;
		try{
		name = newBlogger.bindFromRequest().get().name;
		desc = newBlogger.bindFromRequest().get().description;
		longDesc = newBlogger.bindFromRequest().get().longDescription;
		} catch(IllegalStateException e) {
			flash("add_product_null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
			return redirect(routes.BloggerController.addBlog());
		}
		User u = SessionHelper.getCurrentUser(ctx());
		Blogger b = Blogger.create(name, desc, longDesc);
		return redirect("/addBlogPicture/" + b.id);
	}
	
//	public static Result showBlogPage(){
//		List<Blogger> bloggerList = Blogger.find.all();
//		return ok(blog.render(bloggerList));
//	}
	
	
	
public static Result saveFile(int id){
		
	   	Blogger b = findBlogger.byId(id);
	   	User u = SessionHelper.getCurrentUser(ctx());
	   	
	  //checks if picture exists in base, if it does deletes it then uploads new picture
	  		final String deletePath = "." + File.separator 
	  				+ "public" + File.separator;
	  		String s = findBlogger.byId(id).blogImagePath;
	  		String defaultPic = "images" + File.separator + "blogPicture" + File.separator + "no-img.png";
	  		
	  		if (s != null && !s.equals(defaultPic)){
	  			File d = new File(deletePath + s);
	  			d.delete();
	  		}
   	  	//creating path where we are going to save image
		final String savePath = "." + File.separator 
				+ "public" + File.separator + "images" 
				+ File.separator + "blogPicture" + File.separator;
		
		//it takes uploaded information  
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("image");
		if (filePart == null){
			 flash("error",  Messages.get("Niste uploadovali sliku"));
			 return redirect("/addBlogPicture/" + id);
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
			return redirect("/addBlogPicture/" + id);
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){

			flash("error",  Messages.get("Slika ne smije biti veca od 2 MB"));
			Logger.of("blog").warn( u.username + " tried to upload an image that is bigger than 2MB.");

			flash("error",  Messages.get("Slika ne smije biti veća od 2 MB"));
			Logger.of("blog").warn( u.username + " tried to upload an image that is bigger than 2MB.");

			return redirect("/addBlogPicture/" + id);
		}
		
		//creating image name from user id, and take image extension, than move image to new location
		try {
			File profile = new File(savePath + UUID.randomUUID().toString() + extension);
			Files.move(image, profile );		
			String assetsPath = "images" 
					+ File.separator + "blogPicture" + File.separator + profile.getName();
			b.setBlogImagePath(assetsPath);
			
			
			b.save();
		} catch (IOException e) {
			Logger.of("product").error( u.username + " failed to upload an image to the product " +b.name);
			e.printStackTrace();
		}
		

		flash("add_product_success", Messages.get("Uspjesno ste uploadali sliku"));

		flash("add_product_success", Messages.get("Uspješno ste objavili oglas"));


		return redirect("/showBlog");
	}
			public static Result bloggerPicture(int id) {
					Blogger b = findBlogger.byId(id);
					return ok(addBlogPicture.render(b));
}
			
			public static Result editBlog(int id){
				 User currentUser = SessionHelper.getCurrentUser(ctx());
				 Blogger b = findBlogger.byId(id);
				 List<Blogger> bloggerList = Blogger.find.all();
				 if (b == null) {
				 		Logger.of("product").warn("Failed try to update a product which doesnt exist");
				   		return redirect(routes.Application.index());
				   	 }
				 if (currentUser == null ) {
				   		Logger.of("product").warn("Not blogger tried to update a blog");
				   		return redirect(routes.Application.index());
					}
					//  Ako je admin ulogovan, onemogucujemo mu da edituje proizvod;
				   	if (currentUser.isAdmin==true) {
						Logger.of("product").warn("An admin tried to update a blog");
			 			return redirect(routes.Application.index());
			   		}
				//  Prosle sve provjere, tj. dozvoljavamo samo registrovanom useru <svog proizvoda> da ga edituje;    
			   		return ok(editBlog.render(b, bloggerList));
			}
			
		public static Result saveEditedBlog(int id){
			String name;
			String desc;
			String longDesc;
			try{
				name = newBlogger.bindFromRequest().get().name;
				desc = newBlogger.bindFromRequest().get().description;
				longDesc = newBlogger.bindFromRequest().get().longDescription;
			} catch(IllegalStateException e) {		
				flash("edit_blog_null_field", Messages.get("Molim Vas popunite sva polja u formi."));
				return redirect(routes.BloggerController.editBlog(id));
			}
			Blogger b = findBlogger.byId(id);
			String oldName = b.name;
			b.setName(name);
			b.setDescription(desc);
			b.setLongDescription(longDesc);
			User u = SessionHelper.getCurrentUser(ctx());
			b.save();
			Logger.of("product").info("User "+ u.username + " updated the info of product " + oldName + ", NAME : ["+b.name+"]");
			oldName = null;
			flash("edit_blog_success", Messages.get("Uspješno ste izmijenili blog"));
			//return redirect("/showProduct/" + id);	
			return redirect("/addBlogPicture/" + b.id);
		}
		public static Result deleteBlog(int id){
			deletePicture(id);
			Blogger.delete(id);
			flash("delete_blog_success",  Messages.get("Uspješno ste izbrisali blog"));
			  if (!request().accepts("text/html")) {
					return ok();
			  }
			  return redirect(routes.BloggerController.showBlog());
			
		}
		public static void deletePicture(int id){
			final String deletePath = "." + File.separator 
					+ "public" + File.separator;
			String defaultPic = "images" + File.separator + "blogPicture" + File.separator + "no-img.jpg";
					String s = findBlogger.byId(id).blogImagePath;
									
				if (!s.equals(defaultPic)) {
					File file = new File(deletePath + s);
					file.delete();
								
			}			
		}
		public static Result searchBlog(String q){
			User u = helpers.SessionHelper.getCurrentUser(ctx());
			List<Blogger>bloggerList=Blogger.find.where("UPPER(name) LIKE UPPER('%"+q+"%')").findList();
			return ok(blog.render(bloggerList,u));	
		}
	
		
	
}
