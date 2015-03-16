package helpers;

import controllers.routes;
import models.User;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security;

/**
 * security class-when applied on some method, it allows only users with
 * administrator rights to use that method
 * 
 * @author user
 *
 */
public class AdminFilter extends Security.Authenticator {
	/**
	 * gets username from context object for only those users with admin rights
	 */
	@Override
	public String getUsername(Context ctx) {
		if (ctx.session().containsKey("username")) {
			User u = User.finder(ctx.session().get("username"));
			if (u != null && Boolean.TRUE.equals(u.isAdmin)) {
				return u.username;
			}
		}
		return null;
	}

	/**
	 * redirects unauthorized users to the specified page
	 */
	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.Application.index());
	}

}