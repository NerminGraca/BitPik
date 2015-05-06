package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class TransactionP extends Model {
	
	@Id
	public int id;
	
	public String token;
	
	public String seller_comment;
	
	public String buyer_comment;
	
	public double buyer_value;
	
	public String buyer_stringValue;
	
	public double seller_value;
	
	public String seller_stringValue;
	
	public String dateTransaction;
	
	@OneToOne
	public Product product;
	
	
	/**
	 * The default Constructor;
	 */
	public TransactionP () {
		this.token = "this is not a transaction";
		this.seller_comment = "Ne postoji";
		this.buyer_comment = "Ne postoji";
		this.buyer_value=0;
		this.seller_value=0;
		this.dateTransaction = getDate();
		this.product = null;
	}
	
	/**
	 * Constructor receiving only the token;
	 * After the Paypal transaction of the purchase;
	 * @param token
	 */
	public TransactionP(String token, Product product) {
		this.token = token;
		this.seller_comment = "Ne postoji";
		this.buyer_comment = "Ne postoji";
		this.buyer_value=0;
		this.seller_value=0;
		this.dateTransaction = getDate();
		this.product = product;
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
	
	public double getBuyer_value(){
		return buyer_value;
	}
	
	public void setBuyer_value(double buyer_value){
		this.buyer_value=buyer_value;
	}
	
	public double getSeller_value(){
		return seller_value;
	}
	
	public void setSeller_value(double seller_value){
		this.seller_value=seller_value;
	}
	
	/**
	 * After the item has been sold/bought we create the transaction and 
	 * put the date into that transaction when has the transaction taken
	 * efect.
	 * @return dateOfTransaction;
	 */
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}

	/**
	 * Gets the productId of the transaction;
	 * @return
	 */
	public Product getProductId() {
		return product;
	}	
	
	public double setValue(String stringValue){
		double value=0;
		if(stringValue.equals("Pozitivan")){
			value=1;
			}
		else if(stringValue.equals("Negativan")){
			value=-1;
		}
		return value;
		}
	
	public void setBuyer_StringValue(double value){
		String stringValue="Neutralan";
		if(value>0){
			stringValue="Pozitivan";
			}
			else if(value<0){
				stringValue="Negativan";
			}
		 buyer_stringValue=stringValue;
		}
	
	public void setSellerStringValue(double value){
		String stringValue="Neutralan";
		if(value>0){
			stringValue="Pozitivan";
			}
			else if(value<0){
				stringValue="Negativan";
			}
		 seller_stringValue=stringValue;
		}
	
	public String getBuyer_StringValue(){
		return buyer_stringValue;
	}
	
	public String getSeller_StringValue(){
		return seller_stringValue;
	}
	}
	

