package models;

import javax.persistence.*;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class User extends Model {
	
	@Id
	public int id;
	
	@Required
	public String username;
	
	@Required
	public String password;
	
	@Email
	public String email;
	
	
	/**
	 * Constructor
	 * Creates a new User
	 * @param username
	 * @param password
	 */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	/**
	 * Creates a new user and saves the User into database;
	 * @param username
	 * @param password
	 */
	public static void create(String username, String password, String email) {
		new User(username, password, email).save();
	}

	// Finder
	static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	static Finder<Integer, User> findInt = new Finder<Integer, User>(Integer.class, User.class);
	/**
	 * Finds the User under the username(parameter) in the database;
	 * @param username
	 * @return
	 */
	public static User finder(String username) {
		return find.where().eq("username", username).findUnique();
	}
	
	public static User find(int id) {
		return findInt.byId(id);
	}
	
	public static void delete(int id) {
		findInt.byId(id).delete();
	}
}
