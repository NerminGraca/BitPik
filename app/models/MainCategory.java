package models;

import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class MainCategory extends Model {
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	@OneToMany(mappedBy="mainCategory", cascade=CascadeType.ALL)
	public List<Product> products;
	
	@OneToMany(mappedBy="mainCategory", cascade=CascadeType.ALL)
	public List<SubCategory> subCategories;
	
	@OneToMany(mappedBy="storeCategory", cascade=CascadeType.ALL)
	public User pikStore;
	/**
	 * Default constructor
	 */
	public MainCategory() {
		this.name = "Unknown";
	}
	
	/**
	 * Constructor of object MainCategory with one parameter (String name)
	 * @param name = String name which will new object hold
	 */
	public MainCategory(String name) {
		this.name = name;		
	}
	
	/**
	 * Method createMainCategory creates new object of class MainCategory and saves it
	 * in the database
	 * @param name = String name which will new object hold
	 */
	public static void createMainCategory(String name) {
		new MainCategory(name).save();
	}

	public static Finder<Integer, MainCategory> find = new Finder<Integer, MainCategory>(Integer.class, MainCategory.class);
	public static Finder<String, MainCategory> findByName = new Finder<String, MainCategory>(String.class, MainCategory.class);
	
	/**
	 * Method finds MainCategory by its Id
	 * @param id
	 * @return
	 */
	public static MainCategory findMainCategory(int id) {
		return find.byId(id);
	}
	
	/**
	 * Method finds MainCategory by its name
	 * @param name
	 * @return
	 */
	public static MainCategory findMainCategoryByName(String name) {
		return findByName.where().eq("name", name).findUnique();
	}
	
	/**
	 * Method returns all of MainCategory instances
	 * @return
	 */
	public static List<MainCategory> allMainCategories() {
		List<MainCategory> allMainCategories = find.all();
		return allMainCategories;
	}

	/**
	 * Method deletes MainCategory
	 * @param id
	 */
	public static void delete(int id) {
		  find.ref(id).delete();
	}
	
	/**
	 * Setter for value name in object MainCategory
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
