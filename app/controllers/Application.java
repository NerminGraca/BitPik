package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

  static String message = "Welcome";
  final static Form<User> userForm = form(User.class);
	
  public static Result index() {
    return ok(index.render(message, userForm));
  }
  
  public static Result registration() {
	Form<User> newUser = userForm.bindFromRequest();
  	return ok(registration.render(newUser));
  }
  
  public static Result login() {
  	return ok(login.render());
  }
  
  public static Result addUser() {
  	String username = userForm.bindFromRequest().get().username;
  	String password = userForm.bindFromRequest().get().password;
  	User.create(username, password);
  	return redirect("/");
  	
  }
  
  public static Result findUser() {
  	String username = userForm.bindFromRequest().get().username;
  	String password = userForm.bindFromRequest().get().password;
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
	String message = "Success!";
  	return ok(success.render(message));
  }
  
  public static Result failed() {
	String message = "Failed!";
  	return ok(failed.render(message));
  }
}
