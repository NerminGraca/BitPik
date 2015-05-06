package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class BlogTag extends Model{
	
	@Id
	public int id;
	@Required
	public String tag;
	@ManyToMany
	public List<Blogger> blogger;
	
	public static Finder<Integer, BlogTag> find = new Finder<Integer, BlogTag>(Integer.class, BlogTag.class);
	
	public BlogTag(String tag, Blogger blogger){
		this.tag = tag;
		if(this.blogger == null){
			this.blogger = new ArrayList<Blogger>();
		}
		this.blogger.add(blogger);
	
	}
	
	
	public BlogTag(String tag){
		this.tag = tag;
		if(this.blogger == null){
			this.blogger = new ArrayList<Blogger>();
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public List<Blogger> getBlogger() {
		return blogger;
	}
	public void setBlogger(List<Blogger> blogger) {
		this.blogger = blogger;
	}

}
