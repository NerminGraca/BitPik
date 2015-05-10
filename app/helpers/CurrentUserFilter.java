package helpers;

import controllers.routes;
import models.User;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

public class CurrentUserFilter extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		if(!ctx.session().containsKey("username"))
			return null;
		String username = ctx.session().get("username");
		User u = User.finder(username);
		if (u != null)
			return u.username;
		return null;
	}

	/**
	 * Redirects unauthorized users to the specified page
	 */
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.index());
	}

}
