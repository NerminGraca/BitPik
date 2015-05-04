package models;

import helpers.HashHelper;
import helpers.MailHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	
	public boolean isPikStore;
	
	public String storeName;
	
	public String address;
	
	public String city;
	
	public String phone;

	public String categoryString;
	
	@ManyToOne
	public MainCategory storeCategory;
	
	public boolean isProtectedAdmin;

	public String createdDate;
	
	@OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
	public List<Product> products;
	
	public boolean verified;
	
	public String confirmation;
	
	public boolean emailVerified;
	
	public String emailConfirmation;
	
	@OneToOne(mappedBy="creditOwner", cascade=CascadeType.ALL)
	public BPCredit bpcredit;
	
	public String imagePath;
	
	@OneToOne(mappedBy="userImage", cascade=CascadeType.ALL)
	public ImgPath imagePathOne;
	
	@OneToMany(mappedBy="buyerUser", cascade=CascadeType.ALL)
	public List<Product> bought_products;

	
	@OneToMany(mappedBy="sender", cascade=CascadeType.ALL)
	public List<PrivateMessage> privateMessage;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Newsletter> newsletter;
	
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
		isProtectedAdmin = false;
		createdDate = getDate();
		this.verified = false;
		this.emailVerified = false;
		this.imagePath = "images/profilePicture/profileimg.png";
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
		isProtectedAdmin = false;
		createdDate = getDate();
		this.verified = false;
		this.confirmation = UUID.randomUUID().toString();
		this.emailVerified = false;
		this.emailConfirmation = UUID.randomUUID().toString();
		this.imagePath = "images/profilePicture/profileimg.png";
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
		isProtectedAdmin = isAdmin;
		createdDate = getDate();
		this.verified = true;
		this.confirmation = null;
		this.emailVerified = true;
		this.imagePath = "images/profilePicture/profileimg.png";
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
		user.setCredits(new BPCredit(user));
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
		newUser.setCredits(new BPCredit(newUser));
		newUser.save();
		
		MailHelper.send(email,"http://localhost:9000/confirm/" + newUser.confirmation);
		return newUser;
	}
	
	public static User createPikStore(String username, String password,String email,String storeName,String address,String city,MainCategory storeCategory){
		User newUser = new User(username,password,email);
		newUser.setPikStore();
		newUser.storeName=storeName;
		newUser.address=address;
		newUser.city=city;
		newUser.setCredits(new BPCredit(300, newUser));
		newUser.setStoreCategory(storeCategory);
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
	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);
	public static Finder<Integer, User> findInt = new Finder<Integer, User>(Integer.class, User.class);
	/**
	 * Finds the User under the username(parameter) in the database;
	 * @param username
	 * @return
	 */
	public static User finder(String username) {
		// promjena mala jer je nesto zeznuto radi i ovako!
		List u = find.where().eq("username", username).findList();
		if (u.size()==0) {
			return null;
		}
		return (User)(u.get(0));
		
		//return find.where().eq("username", username).findUnique();
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
		User.find(id).delete();
		//findInt.ref(id).delete();
	}
	
	/**
	 * Setter for the isAdmin;
	 */
	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
		save();
			
	}
	public void setPikStore()
	{
		this.isPikStore=true;
		save();
	}
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void setStoreCategory(MainCategory storeCategory){
		this.storeCategory=storeCategory;
	}
	
	/**
	 * Gets the BPCredits of the User;
	 * @return
	 */
	public BPCredit getCredits() {
		return bpcredit;
	}

	/**
	 * Sets the BPCredits of the User;
	 * @param credits
	 */
	public void setCredits(BPCredit credits) {
		this.bpcredit = credits;
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
	
	public String userSellerValue(){
		double value=0;
		double sum=0;
		try {
			for(Product product: products){
				if(product.isSold){
				sum+=product.purchaseTransaction.getBuyer_value();
				}
			}
		} catch (NullPointerException e) {
			sum=0;
		}
		value=sum/products.size();
		
		if(value<0){
			return "Negativan";
		}
		else if(value>0){
			return "Pozitivan";
		}
		return "Neutralan";
	}
	
	public String userBuyerValue(){
		double value=0;
		double sum=0;
		try {
			for(Product product: bought_products){
				
				sum+=product.purchaseTransaction.getSeller_value();
			}
		} catch (NullPointerException e) {
			
			sum=0;
		}
		value=sum/bought_products.size();
		
		if(value<0){
			return "Negativan";
		}
		else if(value>0){
			return "Pozitivan";
		}
		return "Neutralan";
	}

}
