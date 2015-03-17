package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Product extends Model {
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	public String description;
	
	@Required
	public String categoryString;
	
	@ManyToOne
	public MainCategory mainCategory;
	
	@Required
	public double price;
	
	public String publishedDate;
	
	@ManyToOne
	public User owner;
	
	@Required
	public String availability;
	
	@ManyToOne
	public SubCategory subCategory;
	
	public String subCategoryString;
	
	/**
	 * Constructor with default values
	 */
	public Product() {
		this.name = "Unknown";
		this.description = "Unknown";
		this.categoryString = "Unknown";
		this.subCategoryString = "Unknown";
		this.price = -1;
		this.owner = null;
		this.mainCategory = null;
		this.subCategory = null;
		this.availability = "Unknown";
		publishedDate = getDate();
	}

	/**
	 * Constructor of object Product with all 3 parameters.  
	 * @param name
	 * @param desc
	 * @param price
	 */
	public Product(String name, String desc, double price, User owner, MainCategory mainCategory, SubCategory subCategory, String availability) {
		this.name = name;
		this.description = desc;
		this.price = price;
		this.owner = owner;
		this.mainCategory = mainCategory;
		this.subCategory = subCategory;
		this.availability = availability;
		publishedDate = getDate();
	}
	
	//Finder
	public static Finder<Integer, Product> find = new Finder<Integer, Product>(Integer.class, Product.class);
	
	// Setters for all the attributes that can be changed
		// in the editProduct.html page;
	/**
	 * Sets the name of the product;
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Sets the description of the product;
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.description = desc;
	}

	/**
	 * Sets the category of the product;
	 * @param mainCategory
	 */
	public void setCategory(MainCategory mainCategory) {
		this.mainCategory = mainCategory;
	}

	/**
	 * Sets the price of the product;
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Sets the availability of the product;
	 * @param availability
	 */
	public void setAvailability(String availability) {
		this.availability = availability;
	}
		
	/**
	 * @author Graca Nermin
	 * When the constructor is called. Meaning, when the item/product is published this date will be
	 * created as the publishedDate;
	 * @return publishedDate;
	 */
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	/**
	 * Method creates a new product, by calling the constructor;
	 * @param name
	 * @param desc
	 * @param price
	 */
	public static Product create(String name, String desc, double price, User owner, MainCategory mainCategory, SubCategory subCategory, String availability) {
		Product newProduct = new Product(name, desc, price, owner, mainCategory, subCategory, availability);
		newProduct.save();
		return newProduct;
	}
	
	/**
	 * Method deletes Product which has id given to the method
	 * @param id = id of object Product which will be deleted from database
	 */
	public static void delete(int id) {
		  find.ref(id).delete();
	}
}
