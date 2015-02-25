package models;

import javax.persistence.*;

import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
/**
 * 
 * @author Nermin Graca & Nedzad Hamzic & Neldin Dzekovic
 *
 */
@Entity
public class User extends Model {
	
	@Id
	public int id;
	
	@Required
	@Unique
	public String username;
	
	@Required
	public String password;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public static void create(String username, String password) {
		new User(username, password).save();
	}

	static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	
	public static User finder(String username) {
		return find.where().eq("username", username).findUnique();
	}
}
