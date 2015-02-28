package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Email;
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
	
	public boolean isAdmin;

	public String createdDate;
	
	
	/**
	 * Constructor of object User with three parameters. On default sets isAdmin to false and
	 * creates String representation of date when was it created 
	 * @param username
	 * @param password
	 * @param email
	 */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		isAdmin = false;		
		createdDate = getDate();
	}
	
	/**
	 * @author Graca Nermin
	 * Method creates simple date as string which will be represented on users profile
	 * It will be set once the profile has been created
	 * @return String of current date
	 */
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	/**
	 * Creates a new user and saves the User into database;
	 * @param username
	 * @param password
	 */
	public static void create(String username, String password, String email) {
		new User(username, password, email).save();
	}

	// Finders
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
	
	/**
	 * @author Graca Nermin
	 * Method searches database for given String "email" if it exists in the database,
	 * and returns true or false
	 * @param email
	 * @return Boolean true or false
	 */
	public static boolean emailFinder(String email) {
		List<User> list = find.where().eq("email", email).findList();
		if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static User find(int id) {
		return findInt.byId(id);
	}
	
	public static void delete(int id) {
		findInt.byId(id).delete();
	}
}
