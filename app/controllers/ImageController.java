package controllers;

import java.io.File;

import models.ImgPath;
import models.Product;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;


public class ImageController  extends Controller  {

<<<<<<< HEAD
	/**
	 * Method creates image for user or product
	 * @param image
	 * @param obj
	 */
	
=======
>>>>>>> b0efd60b4c1619211b9d864d5ce8532c97856e60
	public static void create(File image, Object obj){
		
		if (obj instanceof Product) {
			Product p = (Product) obj;
<<<<<<< HEAD
=======
			
>>>>>>> b0efd60b4c1619211b9d864d5ce8532c97856e60
			ImgPath.create(image, p);
		} else if (obj instanceof User) {
			User u = (User) obj;
			ImgPath.create(image, u);
		}
		
		//TODO;
		
	}
	
<<<<<<< HEAD
	/**
	 * Method deletes image
	 * @param id
	 * @return
	 */
=======
>>>>>>> b0efd60b4c1619211b9d864d5ce8532c97856e60
	
	public static Result delete(int id){
		ImgPath.find.byId(id).delete();
		return redirect(routes.Application.index());
	}
	
	
}
