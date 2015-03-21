package models;

import helpers.HashHelper;
import helpers.MailHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
	
	@OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
	public List<Product> products;
	
	public boolean verified;
	
	public String confirmation;
	
	public boolean emailVerified;
	
	public String emailConfirmation;
	
	public String imagePath;
	
	/**
	 * @author Gordan Sajevic
	 * Constructor with default values
	 */
	public User() {
		this.username = "JohnDoe";
		this.password = "johndoe";
		this.password = HashHelper.createPassword(password);
		this.email = "johndoe@example.com";
		isAdmin = false;		
		createdDate = getDate();
		this.verified = false;
		this.emailVerified = false;
		this.imagePath = "images/no-img.jpg";
	}

	/**
	 * Constructor of object User with three parameters. On default sets isAdmin to false and
	 * creates String representation of date when was it created 
	 * @param username
	 * @param password
	 * @param email
	 */
	public User(String username, String password, String email) {
		this.username = username;
		this.password = HashHelper.createPassword(password);
		this.email = email;
		isAdmin = false;		
		createdDate = getDate();
		this.verified = false;
		this.confirmation = UUID.randomUUID().toString();
		this.emailVerified = false;
		this.emailConfirmation = UUID.randomUUID().toString();
		
	}
	
	/**
	 * Constructor which creates new object User which is also a Admin (isAdmin = true)
	 * @param username
	 * @param password
	 * @param email
	 * @param isAdmin
	 */
	public User(String username, String password, String email, boolean isAdmin) {
		this.username = username;
		this.password = HashHelper.createPassword(password);
		this.email = email;
		this.isAdmin = isAdmin;		
		createdDate = getDate();
		this.verified = true;
		this.confirmation = null;
		this.emailVerified = true;
		this.imagePath = "images/no-img.jpg";

	}
	
	/**
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
	public static User create(String username, String password, String email) {
		User user = new User(username, password, email);
		user.save();
		return user;
	}

	/**
	 * Creates a new User object and saves it into database
	 * 
	 * @param username
	 * @param password
	 * @param email
	 */
	public static User createSaveUser(String username, String password,String email) {
		User newUser = new User(username, password,email);
		newUser.save();
		
		MailHelper.send(email,"http://localhost:9000/confirm/" + newUser.confirmation);
		return newUser;
	}
	
	/**
	 * Creates a new Admin object and saves it into database, and returns value of it's ID variable
	 * 
	 * @param username
	 * @param password
	 * @param email
	 */
	public static User createAdmin(String username, String password, String email, boolean isAdmin) {
		User admin = new User(username, password, email, isAdmin);
		admin.save();
		return admin;
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
	
	/**
	 * Finds the User under the id;
	 * @param id
	 * @return
	 */
	public static User find(int id) {
		return findInt.byId(id);
	}
	
	/**
	 * Deletes the User under the id;
	 * @param id
	 */
	public static void delete(int id) {
		findInt.ref(id).delete();
	}
	
	/**
	 * Setter for the isAdmin;
	 */
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
		save();
			
	}
	
	/**
	 * Setter for username
	 * @param username
	 */
	
	public void setUsername(String username)
	{
		if(username.length()<1)
		{
			throw new IllegalArgumentException("Username cannot be empty field!");
		}
		this.username = username;
	}
	
	/**
	 * Setter for email
	 * @param username
	 */
	
	public void setEmail(String email)
	{
		if(!email.contains("@") || !email.contains("."))
		{
			throw new IllegalArgumentException("Email not valid!");
		}
		this.email = email;
	}
	
	/**
	 * Setter for password
	 * @param username
	 */
	
	public void setPassword(String password)
	{
		if(password.length() < 4)
		{
			throw new IllegalArgumentException("Password too short!");
		}
		this.password = password;
	}
	
}
