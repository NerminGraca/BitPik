package controllers;

import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Session extends Security.Authenticator {
	
	@Override
	public String getUsername(Context ctx) {
		if (!ctx.session().containsKey("owner_id")) {
			return null;
		}
		int id = Integer.parseInt(ctx.session().get("owner_id"));
		User u = User.find(id);
		if (u != null) {
			return u.username;
		}
		return null;
	}
	
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.UserController.index());
	}
	
	public static User getCurrentUser(Context ctx) {
		if (!ctx.session().containsKey("owner_id")) {
			return null;
		}
		int id = Integer.parseInt(ctx.session().get("owner_id"));
		User u = User.find(id);
		return u;
	}

}
