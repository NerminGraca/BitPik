package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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
	
	@ManyToMany(mappedBy="blogger", cascade=CascadeType.ALL)
	public List<BlogTag> tag;
	
	public Blogger(){
		this.name="Unknown";
		this.description = "Unknown";
		this.longDescription = "Unknown";
		this.blogImagePath = "images/blogPicture/no-img.jpg";
		this.publishedDate=getDate();
		this.tag = new ArrayList<BlogTag>();
	}
	
	public Blogger(String name, String description, String longDescription,BlogTag tag){
		this.name = name;
		this.description = description;
		this.longDescription = longDescription;
		this.blogImagePath = "images/blogPicture/no-img.jpg";
		this.publishedDate=getDate();
		if(this.tag == null){
			this.tag = new ArrayList<BlogTag>();
		}
		this.tag.add(tag);
	}
	
	public List<BlogTag> getTag() {
		return tag;
	}

	public void setTag(List<BlogTag> tag) {
		this.tag = tag;
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
	
	public static Blogger create(String name, String description, String longDescription, BlogTag tag){
		Blogger newBlogger =  new Blogger(name, description, longDescription, tag);
		
		newBlogger.save();
		return newBlogger;
	}
	public static void delete(int id) {
		  find.ref(id).delete();
	}

}
