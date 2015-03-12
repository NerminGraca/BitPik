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
	
	public String desc;
	
	@Required
	public String category;
	
	@Required
	public double price;
	
	public String publishedDate;
	
	@ManyToOne
	public User owner;
	
	@Required
	public String availability;
	
	/**
	 * Constructor with default values
	 */
	public Product() {
		this.name = "proizvod";
		this.desc = "opis";
		this.price = -1;
		this.owner = new User();
		this.category = "Ostale kategorije";
		this.availability = "Nedođija";
		publishedDate = getDate();
	}

	/**
	 * Constructor of object Product with all 3 parameters.  
	 * @param name
	 * @param desc
	 * @param price
	 */
	public Product(String name, String desc, double price, String category, String availability, User owner) {
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.owner = owner;
		this.category = category;		
		this.availability = availability;
		publishedDate = getDate();
	}
	
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
			this.desc = desc;
		}

		/**
		 * Sets the category of the product;
		 * @param category
		 */
		public void setCategory(String category) {
			this.category = category;
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
	public static Product create(String name, String desc, double price, String category, String availability, User owner) {
		Product newProduct = new Product(name, desc, price, category, availability, owner);
		newProduct.save();
		return newProduct;
	}
	
	public static Finder<Integer, Product> find = new Finder<Integer, Product>(Integer.class, Product.class);
	
	public static void delete(int id) {
		  find.ref(id).delete();
	}
}
