package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BPCredit {
	@Id
	public int id;
	
	public int credit;
	
	@OneToOne
	public User creditOwner;
	
	/**
	 * Default contructor
	 */
	public BPCredit() {
		this.credit = 0;
		this.creditOwner = null;
	}
	
	/**
	 * Constructor with two parameters;
	 * @param credit
	 * @param creditOwner
	 */
	public BPCredit(User creditOwner) {
		this.credit = 0;
		this.creditOwner = creditOwner;
	}

	/**
	 * Gets the credits of the user;
	 * @return
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * Sets the credit of the BPCredit;
	 * @param credit
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}

	/**
	 * Gets the user of the BPCredit;
	 * @return
	 */
	public User getCreditOwner() {
		return creditOwner;
	}
	
	/**
	 * Sets the user of the BPCredit;
	 * @param creditOwner
	 */
	public void setCreditOwner(User creditOwner) {
		this.creditOwner = creditOwner;
	}
	
		

}
