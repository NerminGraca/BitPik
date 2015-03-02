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
	public double price;
	
	public String publishedDate;
	
	/**
	 * Constructor of object Product with 2 parameters.
	 * *(without description);
	 * @param name
	 * @param price
	 */
	public Product(String name, double price) {
		//TODO dodati kategoriju i dostuonost
		this.name = name;
		this.desc = "";
		this.price = price;
		publishedDate = getDate();
	}
	
	/**
	 * Constructor of object Product with all 3 parameters.  
	 * @param name
	 * @param desc
	 * @param price
	 */
	public Product(String name, String desc, double price) {
		this.name = name;
		this.desc = desc;
		this.price = price;
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
	
	public static void create(String name, String desc, double price) {
		new Product(name, desc, price).save();;
	}
}
