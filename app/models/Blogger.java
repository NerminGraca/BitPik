package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class Blogger extends Model{
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	public String description;
	
	@Column(columnDefinition = "TEXT")
	public String longDescription;
	
	public String blogImagePath;
	
	public String publishedDate;
	
	public Blogger(){
		this.name="Unknown";
		this.description = "Unknown";
		this.longDescription = "Unknown";
		this.blogImagePath = "images/blogPicture/no-img.jpg";
		this.publishedDate=getDate();
	}
	
	public Blogger(String name, String description, String longDescription){
		this.name = name;
		this.description = description;
		this.longDescription = longDescription;
		this.blogImagePath = "images/blogPicture/no-img.jpg";
		this.publishedDate=getDate();
	}
	
	public static Finder<Integer, Blogger> find = new Finder<Integer, Blogger>(Integer.class, Blogger.class);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getBlogImagePath() {
		return blogImagePath;
	}

	public void setBlogImagePath(String blogImagePath) {
		this.blogImagePath = blogImagePath;
		this.save();
	}

	public static Finder<Integer, Blogger> getFind() {
		return find;
	}

	public static void setFind(Finder<Integer, Blogger> find) {
		Blogger.find = find;
	}
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	public static Blogger create(String name, String description, String longDescription){
		Blogger newBlogger =  new Blogger(name, description, longDescription);
		
		newBlogger.save();
		return newBlogger;
	}
	public static void delete(int id) {
		  find.ref(id).delete();
	}

}
