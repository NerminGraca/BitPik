package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;
import models.User;

 
public class SessionHelper extends Security.Authenticator {
	
	public static User getCurrentUser(Context ctx){
		String username = ctx.session().get("username");
		if(username == null)
			return null;
		return User.finder(username);
	}
	
	@Override
	public Result onUnauthorized(Context ctx){
		return redirect(routes.Application.index());
	}
	
	public static boolean isAdminCheck(Context ctx) {

		if(getCurrentUser(ctx) == null)
			return false;
		if(getCurrentUser(ctx).isAdmin==true)
			return true;
		else
			return false;
	}
	

}
