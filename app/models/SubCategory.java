package models;

import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class SubCategory extends Model {
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	@ManyToOne
	public MainCategory mainCategory;
	
	@OneToMany(mappedBy="subCategory", cascade=CascadeType.ALL)
	public List<Product> products;
	
	/**
	 * Default constructor
	 */
	public SubCategory() {
		this.name = "Unknown";
		this.mainCategory = null;
	}
	
	/**
	 * Constructor of object SubCategory with one parameter (String name)
	 * @param name = String name which will new object hold
	 */
	public SubCategory(String name, MainCategory mainCategory) {
		this.name = name;
		this.mainCategory = mainCategory;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public static void createSubCategory(String name, MainCategory mainCategory) {
		new SubCategory(name, mainCategory).save();
	}
	
	public static Finder<Integer, SubCategory> find = new Finder<Integer, SubCategory>(Integer.class, SubCategory.class);
	public static Finder<String, SubCategory> findByName = new Finder<String, SubCategory>(String.class, SubCategory.class);
	
	public static SubCategory findSubCategory(int id) {
		return find.byId(id);
	}
	
	public static SubCategory findSubCategoryByName(String name) {
		return findByName.where().eq("name", name).findUnique();
	}
	
	public static boolean findSubCategoryByNameAndMainCategory(String name, MainCategory mc) {
		List<SubCategory> subcategories = mc.subCategories;
		Iterator<SubCategory> iter = subcategories.iterator();
		while (iter.hasNext()) {
			SubCategory temp = iter.next();
			if (temp.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static void delete(int id) {
		  find.ref(id).delete();
	}

}
