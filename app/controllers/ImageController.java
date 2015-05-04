package controllers;

import java.io.File;

import models.ImgPath;
import models.Product;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;


public class ImageController  extends Controller  {

	public static void create(File image, Object obj){
		
		if (obj instanceof Product) {
			Product p = (Product) obj;
			ImgPath.create(image, p);
		} else if (obj instanceof User) {
			User u = (User) obj;
			ImgPath.create(image, u);
		}
		
		//TODO;
		
	}
	
	
	public static Result delete(int id){
		ImgPath.find.byId(id).delete();
		return redirect(routes.Application.index());
	}
	
	
}
