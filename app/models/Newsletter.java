package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Newsletter extends Model{
	
	@Id
	public int id;
	
	@Required
	public String searchString;
	
	@Required
	public Date createdDate;
	
	@ManyToOne
	public User user;

	public Newsletter(){
		this.searchString = "no content";
		this.createdDate = null;
		this.user = null;
	}
	
	public Newsletter(String searchString, Date createdDate, User user) {
		this.searchString = searchString;
		this.createdDate = createdDate;
		this.user = user;
	}

	public static Finder<Integer, Newsletter> find = new Finder<Integer, Newsletter>(Integer.class, Newsletter.class);
	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
