package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

/**
 * 
 * @author Nermin Graca & Nedzad Hamzic & Neldin Dzekovic
 *
 */
public class Application extends Controller {
	
	static Form<User> newUser = new Form<User>(User.class);

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result registration() {
    	return ok(registration.render());
    }
    
    public static Result login() {
    	return ok(login.render());
    }
    
    public static Result addUser() {
    	String username = newUser.bindFromRequest().get().username;
    	String password = newUser.bindFromRequest().get().password;
    	User.create(username, password);
    	return redirect("/");
    	
    }
    
    public static Result findUser() {
    	String username = newUser.bindFromRequest().get().username;
    	String password = newUser.bindFromRequest().get().password;
    	User u = User.finder(username);
    	if (u == null) {
    		return redirect("/failed");
    	} else {
    		if (u.password.equals(password)) {
    			return redirect("/success");
    		} else {
    			return redirect("/failed");
    		}
    		
    	}
    }
    
    public static Result success() {
    	return ok(success.render());
    }
    
    public static Result failed() {
    	return ok(failed.render());
    }

}
