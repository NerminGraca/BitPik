package helpers;

import controllers.routes;
import models.User;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security;

public class AdminFilter extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		if (!ctx.session().containsKey("username"))
			return null;
		String username = ctx.session().get("username");
		User u = User.finder(username);
		if (u != null) {
			if (u.isAdmin == true)
				return u.username;
			else
				return null;
		}
		return null;
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.index());
	}

}