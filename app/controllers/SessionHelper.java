package controllers;

import play.mvc.Http.Context;
import models.User;

public class SessionHelper {
	
	public static User currentUser(Context ctx){
		String username = ctx.session().get("username");
		if(username == null)
			return null;
		return User.finder(username);
	}

}
