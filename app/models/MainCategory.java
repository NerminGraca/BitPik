package models;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MainCategory extends Model {
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	/**
	 * @author Graca Nermin
	 * Constructor of object MainCategory with one parameter (String name)
	 * @param name = String name which will new object hold
	 */
	public MainCategory(String name) {
		this.name = name;		
	}
	
	/**
	 * @author Graca Nermin
	 * Method createMainCategory creates new object of class MainCategory and saves it
	 * in the database
	 * @param name = String name which will new object hold
	 */
	public static void createMainCategory(String name) {
		new MainCategory(name).save();
	}
	
	//Finder
	public static Finder<Integer, MainCategory> find = new Finder<Integer, MainCategory>(Integer.class, MainCategory.class);
	
	public static MainCategory findMainCategory(int id) {
		return find.byId(id);
	}

}
