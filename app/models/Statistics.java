package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import models.*;
import models.Product;

import play.db.ebean.Model;

@Entity
public class Statistics extends Model{
	
	@Id
	public int id;
	
	public int noOfClicks;
	
	public String noOfClicksS;
	
	public int noOfComments;
	
	public String isItSold;
	
	public String isItSpecial;
	
	@OneToOne
	public Product productStats;
	
	/**
	 * Contructor with fields;
	 * @param productStats
	 */
	public Statistics() {
		super();
		this.noOfClicks = 0;
		this.noOfClicksS = "0";
		this.noOfComments = 0;
		this.isItSold = "Ne";
		this.isItSpecial = "Ne";
	}

	/**
	 * Contructor with fields;
	 * @param productStats
	 */
	public Statistics(Product productStats) {
		super();
		this.noOfClicks = 0;
		this.noOfClicksS = "0";
		this.noOfComments = 0;
		this.isItSold = "Ne";
		this.isItSpecial = "Ne";
		this.productStats = productStats;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNoOfClicks() {
		return noOfClicks;
	}

	public void setNoOfClicks(int noOfClicks) {
		this.noOfClicks = noOfClicks;
	}

	public int getNoOfComments() {
		return noOfComments;
	}

	public void setNoOfComments(int noOfComments) {
		this.noOfComments = noOfComments;
	}

	public Product getProductStats() {
		return productStats;
	}

	public void setProductStats(Product productStats) {
		this.productStats = productStats;
	}
	
	public String getIsItSold() {
		return isItSold;
	}

	public void setIsItSold(String isItSold) {
		this.isItSold = isItSold;
	}
	
	public String getIsItSpecial() {
		return isItSpecial;
	}

	public void setIsItSpecial(String isItSpecial) {
		this.isItSpecial = isItSpecial;
	}
	
	public String getNoOfClicksS() {
		return noOfClicksS;
	}

	public void setNoOfClicksS(String noOfClicksS) {
		this.noOfClicksS = noOfClicksS;
	}
	
	
	
	

}
