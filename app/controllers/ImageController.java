package controllers;

import java.io.File;
import models.ImgPath;
import play.mvc.Controller;
import play.mvc.Result;


public class ImageController  extends Controller  {

	public static void create(File image){
		ImgPath.create(image);
		
	}
	
	
	public static Result delete(int id){
		ImgPath.find.byId(id).delete();
		return redirect(routes.Application.index());
	}
	
	
}
