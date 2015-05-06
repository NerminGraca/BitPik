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

import models.BlogTag;
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


/**
 * BloggerController class; Contains our methods through which we control all
 * action concerning the Bitpik blog;
 */
public class BloggerController extends Controller{
	
	static Form<Blogger> newBlogger = new Form<Blogger>(Blogger.class);
	static Finder<Integer, Blogger> findBlogger = new Finder<Integer, Blogger>(Integer.class, Blogger.class);
	static Finder<Integer, BlogTag> findBlogTag = new Finder<Integer, BlogTag>(Integer.class, BlogTag.class);
	static Form<BlogTag> newBlogTag = new Form<BlogTag>(BlogTag.class);

	/**
	 * Method showBlog() renders the blog.html page with the following
	 * parameters, correct - The list of all te blogs from our database; listing
	 * in the order from the newly added to the old ones;
	 * 
	 * @return Result rendering the blog.html
	 */
	public static Result showBlog() {
		User u = helpers.SessionHelper.getCurrentUser(ctx());
		List<BlogTag> tagList= BlogTag.find.where("UPPER(tag) LIKE UPPER('%"+"%')").findList();
		List<Blogger> bloggerList = Blogger.find.setMaxRows(3).orderBy("id DESC").findList();
		
		return ok(blog.render(bloggerList,u));
	}
	
	public static Result showBlogAll(){
		User u = helpers.SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.order("id DESC").findList();
		List<BlogTag> tagList= BlogTag.find.where("UPPER(tag) LIKE UPPER('%"+"%')").findList();
		
		return ok(blog.render(bloggerList,u));
	}
	
	/**
	 * Method showOneBlog(int id) receives the parameters integer id, finds the
	 * choosen blog to view by the id and renders the page oneBlog.html
	 * Rendering the page with the parameters > the list of all the blogs,
	 * current user and the choosen blog to view;
	 * 
	 * @param id
	 * @return Result rendering the oneBlog.html
	 */
	public static Result showOneBlog(int id) {
		User u = helpers.SessionHelper.getCurrentUser(ctx());
		BlogTag tag = findBlogTag.byId(id);
		
		List<Blogger> bloggerList = Blogger.find.order("id DESC").findList();
		//List<BlogTag> tagList = (List<BlogTag>) BlogTag.find.byId(id);
		
		Blogger b = findBlogger.byId(id);
		return ok(oneBlog.render(b,u,bloggerList,tag));
	}

	/**
	 * Method addBlog() adds a blog to our list of all blogs and saves to
	 * database through the method createBlog() located here in the
	 * BlgggerController;
	 * 
	 * @return Result rendering the createBlog page
	 */
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
	
	/**
	 * Method createBlog() takes the entered values from the form that the
	 * blogger has entered and creates (instanciates) the Blogger object, thus
	 * making one Blog and saving it to the database;
	 * 
	 * @return Result redirecting to the next page where the blogger has a
	 *         choice to add a picture to the blog he has just added;
	 */
	public static Result createBlog(){
		String name;
		String desc;
		String longDesc;
		String tag;
		try{
		name = newBlogger.bindFromRequest().get().name;
		desc = newBlogger.bindFromRequest().get().description;
		longDesc = newBlogger.bindFromRequest().get().longDescription;
		tag = newBlogTag.bindFromRequest().get().tag;
		} catch(IllegalStateException e) {
			flash("add_product_null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
			return redirect(routes.BloggerController.addBlog());
		}
		User u = SessionHelper.getCurrentUser(ctx());
		BlogTag bTag = new BlogTag(tag);
		Blogger b = Blogger.create(name, desc, longDesc,bTag);
		
		return redirect("/addBlogPicture/" + b.id);
	}
//	public static Result skipPicture(){
//		String name;
//		String desc;
//		String longDesc;
//		String tag;
//		try{
//		name = newBlogger.bindFromRequest().get().name;
//		desc = newBlogger.bindFromRequest().get().description;
//		longDesc = newBlogger.bindFromRequest().get().longDescription;
//		tag = newBlogTag.bindFromRequest().get().tag;
//		} catch(IllegalStateException e) {
//			flash("add_product_null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
//			return redirect(routes.BloggerController.addBlog());
//		}
//		User u = SessionHelper.getCurrentUser(ctx());
//		Blogger b = Blogger.create(name, desc, longDesc,tag);
//		return redirect("/showBlog");
//	}
	
	
	/**
	 * Method saveFile() i called if the blogger has choosen to upload a picture
	 * to the blog he has just added through the method addBlog() &
	 * createBlog(); And in the case if the blogger has choosen to skip this
	 * adding the picture our saveFile() method sets the picture of the just
	 * added blog to the default picture This method receives the id of the just
	 * created Blog;
	 * 
	 * @param id
	 * @return Result rendering the showBlog.html
	 */
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
			 flash("error",  Messages.get("Niste uploadovali sliku."));
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
		
			flash("error",  Messages.get("Niste unijeli sliku."));
			Logger.of("product").warn( u.username + " tried to upload an image that is not valid.");
			return redirect("/addBlogPicture/" + id);
		}
		
		//If file size is bigger then 2MB, redirect user on profile without uploading image.
		double megabyteSize = (image.length() / 1024) / 1024;
		if(megabyteSize > 2){

			flash("error",  Messages.get("Slika ne smije biti veća od 2 MB."));
			Logger.of("blog").warn( u.username + " tried to upload an image that is bigger than 2MB.");

			flash("error",  Messages.get("Slika ne smije biti veća od 2 MB."));
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
	
		flash("add_product_success", Messages.get("Uspješno ste uploadali sliku."));
		flash("add_product_success", Messages.get("Uspješno ste objavili oglas."));

		return redirect("/showBlog");
	}
	
		public static Result bloggerPicture(int id) {
				Blogger b = findBlogger.byId(id);
				return ok(addBlogPicture.render(b));
}
	
	/**
	 * Method editBlog() is called when the blogger has choosen to edit a blog;
	 * The clicked Bloged id is sent to this method; and the editBlog page will
	 * be shown to the blogger. The page editBlog is full of the forms needed in
	 * order that the blogger edits the name, description and the long
	 * description of the blog choosen;
	 * 
	 * @param id
	 * @return Result rendering the editBlog.html
	 */
		public static Result editBlog(int id){
				 User currentUser = SessionHelper.getCurrentUser(ctx());
				 Blogger b = findBlogger.byId(id);
				 List<Blogger> bloggerList = Blogger.find.all();
				// List<BlogTag> blogList	 = BlogTag.find.all();
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
		
	/**
	 * The method saveEditedBlog(int id) is called upon saving the edited blog,
	 * after that the blogger has edited a certain blog of his choice; This
	 * method receives the id of the blog that is being edited;
	 * 
	 * @param id
	 * @return Result rendering the addBlogPicture.html
	 */
		public static Result saveEditedBlog(int id){
			String name;
			String desc;
			String longDesc;
			String tag;
			try{
				name = newBlogger.bindFromRequest().get().name;
				desc = newBlogger.bindFromRequest().get().description;
				longDesc = newBlogger.bindFromRequest().get().longDescription;
				tag = newBlogTag.bindFromRequest().get().tag;
			} catch(IllegalStateException e) {		
				flash("edit_blog_null_field", Messages.get("Molim Vas popunite sva polja u formi."));
				return redirect(routes.BloggerController.editBlog(id));
			}
			Blogger b = findBlogger.byId(id);
//			List<BlogTag> bt = findBlogTag.byId(id);
			
			String oldName = b.name;
			b.setName(name);
			b.setDescription(desc);
			b.setLongDescription(longDesc);
			//bt.setTag(tag);
			//b.setTag(bt);
			
			//b.setTag(tag);
			User u = SessionHelper.getCurrentUser(ctx());
			b.save();
			Logger.of("product").info("User "+ u.username + " updated the info of product " + oldName + ", NAME : ["+b.name+"]");
			oldName = null;
			flash("edit_blog_success", Messages.get("Uspješno ste izmijenili blog."));
			//return redirect("/showProduct/" + id);	
			return redirect("/addBlogPicture/" + b.id);
		}
		
	/**
	 * Method deleteBlog(int id) receives the parameter int id, which is the id
	 * of the blog that the blogger User has choosen to delete;
	 * 
	 * @param id
	 * @return Result redirecting to the showBlog() page;
	 */
		public static Result deleteBlog(int id){
			deletePicture(id);
			Blogger.delete(id);
			flash("delete_blog_success",  Messages.get("Uspješno ste izbrisali blog."));
			  if (!request().accepts("text/html")) {
					return ok();
			  }
			  return redirect(routes.BloggerController.showBlog());
			
		}
		
	/**
	 * This method is called when the blogger User has decided to delete the
	 * picture of a certain blog, The method receives the id of the blog that
	 * the blogger wants to delete and deletes the picture;
	 * 
	 * @param id
	 * 
	 */
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
		
	/**
	 * Method searchBlog(String q) receives a String as a parameter and searches
	 * the list of all the blogs by that name and sends the list found to the
	 * blog.html page
	 * 
	 * @param q
	 * @return Resut rendering the blog.html page
	 */
		public static Result searchBlog(String q){
			User u = helpers.SessionHelper.getCurrentUser(ctx());
			List<Blogger>bloggerList=Blogger.find.where("UPPER(name) LIKE UPPER('%"+q+"%')").findList();
			List<BlogTag> tagList= BlogTag.find.where("UPPER(tag) LIKE UPPER('%"+q+"%')").findList();
			return ok(blog.render(bloggerList,u));	
		}
		
		public static Result searchTag(String q){
			User u = helpers.SessionHelper.getCurrentUser(ctx());
			List<BlogTag> tagList= BlogTag.find.where("UPPER(tag) LIKE UPPER('%"+q+"%')").findList();
			List<Blogger> bloggerList = Blogger.find.order("id DESC").findList();
			
			
			
			return ok(blog.render(bloggerList,u));
		}
	
	
}
