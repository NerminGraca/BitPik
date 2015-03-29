package models;

import javax.persistence.*;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class TransactionP extends Model {
	
	@Id
	public int id;
	
	public String token;
	
	public String seller_comment;
	
	public String buyer_comment;
	
	
	/**
	 * The default Constructor;
	 */
	public TransactionP () {
		this.token = "this is not a transaction";
		this.seller_comment = null;
		this.buyer_comment = null;
	}
	
	/**
	 * Constructor receiving only the token;
	 * After the Paypal transaction of the purchase;
	 * @param token
	 */
	public TransactionP(String token) {
		this.token = token;
		this.seller_comment = null;
		this.buyer_comment = null;
	}
	
	/**
	 * The finder for the Transactions;
	 * Searches by the id (Integer); and 
	 * the finder retruns the Transaction;
	 */
	public static Finder<Integer, TransactionP> find = new Finder<Integer, TransactionP>(Integer.class, TransactionP.class);

	/**
	 * Gets the unique id of the transaction;
	 * @return
	 */
	public int getId() {
		return id;
	}
	/**
	 * Gets the token of the transaction;
	 * @return
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Sets the token of the the transaction;
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the sellers comment of the transaction;
	 * @return
	 */
	public String getSeller_comment() {
		return seller_comment;
	}

	/**
	 * Sets the sellers comment of the transaction;
	 * @param seller_comment
	 */
	public void setSeller_comment(String seller_comment) {
		this.seller_comment = seller_comment;
	}

	/**
	 * Gets the buyers comment of the transaction;
	 * @return
	 */
	public String getBuyer_comment() {
		return buyer_comment;
	}

	/**
	 * Sets the buyers comment of the transaction;
	 *
	 * @param buyer_comment
	 */
	public void setBuyer_comment(String buyer_comment) {
		this.buyer_comment = buyer_comment;
	}


	
	
	
}
