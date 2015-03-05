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
	 * Constructor of object Product with 2 parameters.
	 * *(without description);
	 * @param name
	 * @param price
	 */
	public Product(String name, double price, String category, String availability, User owner) {
		//TODO dodati kategoriju i dostuonost
		this.name = name;
		this.desc = "";
		this.price = price;
		this.owner = owner;
		if(checkCategory(category)==false)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			this.category = category;
		}
		this.availability = availability;
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
		if(checkCategory(category)==false)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			this.category = category;
		}
		
		this.availability = availability;
		publishedDate = getDate();
	}
	
		/**
		 * Constructor with default values
		 */
	
		public Product() {
			//TODO dodati kategoriju i dostuonost
			this.name = "proizvod";
			this.desc = "opis";
			this.price = -1;
			this.owner = new User();
			this.category = "Ostale kategorije";
			this.availability = "Nedođija";
			publishedDate = getDate();
		}
	
	/**
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
	public static int create(String name, String desc, double price, String category, String availability, User owner) {
		Product newProduct = new Product(name, desc, price, category, availability, owner);
		newProduct.save();
		return newProduct.id;
	}
	
	private boolean checkCategory(String category)
	{
		String categoryArray[] = {"Vozila", "Nekretnine", "Mobilni uređaji", 
				"Kompjuteri", "Tehnika", "Nakit i satovi", "Moj dom", "Biznis i industrija",
				"Životinje", "Odjeća i obuća", "Ostale kategorije"};
		for (int i=0; i<categoryArray.length; i++)
		{
			if (category.equalsIgnoreCase(categoryArray[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	static Finder<Integer, Product> find = new Finder<Integer, Product>(Integer.class, Product.class);
	public static void delete(int id) {
		  find.ref(id).delete();
		}
}
