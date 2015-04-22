package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Product extends Model {
	
	@Id
	public int id;
	
	@Required
	public String name;
	
	public String description;
	
	public String longDescription;
	
	@Required
	public String categoryString;
	
	@ManyToOne
	public MainCategory mainCategory;
	
	@Required
	public double price;

	public String publishedDate;
	
	@ManyToOne
	public User owner;
	
	public boolean isSold;
	
	public boolean isRefunding;
	
	public String refundReason;
	
	public boolean refundable;
	
	@Required
	public String availability;
	
	public String condition;
	
	@ManyToOne
	public SubCategory subCategory;
	
	public String subCategoryString;

	public String productImagePath;
	
	@ManyToOne
	public User buyerUser;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	public List<ImgPath> imgPathList;
	
	@OneToOne(mappedBy="product", cascade=CascadeType.ALL)
	public TransactionP purchaseTransaction;
	
	public int credit;
	
	public boolean isSpecial;
	
	public Date expirySpecial;
	
	@OneToOne(mappedBy="productStats", cascade=CascadeType.ALL)
	public Statistics statsProducts;

	/**
	 * Constructor with default values
	 */
	public Product() {
		this.name = "Unknown";
		this.description = "Unknown";
		this.longDescription = "Unknown";
		this.categoryString = "Unknown";
		this.mainCategory = null;
		this.price = -1;
		publishedDate = getDate();
		this.owner = null;
		this.isSold = false;
		this.isRefunding = false;
		this.refundReason = "None";
		this.refundable = true;
		this.availability = "Unknown";
		this.subCategory = null;
		this.subCategoryString = "Unknown";
		this.productImagePath = "images/productPicture/no-img.jpg";
		this.buyerUser = null;
		this.credit = 0;
		this.isSpecial = false;
	}

	/**
	 * Constructor of object Product with all parameters.  
	 * @param name
	 * @param desc
	 * @param price
	 */
	public Product(String name, String desc, String longDesc, double price, User owner, MainCategory mainCategory, SubCategory subCategory, String availability,String condition) {
		this.name = name;
		this.description = desc;
		this.longDescription = longDesc;
		this.price = price;
		this.owner = owner;
		this.isSold = false;
		this.isRefunding = false;
		this.refundReason = "None";
		this.refundable = true;
		this.mainCategory = mainCategory;
		this.subCategory = subCategory;
		this.availability = availability;
		this.condition=condition;
		publishedDate = getDate();
		this.productImagePath = "images/productPicture/no-img.jpg";
		this.buyerUser = null;
		this.credit = 0;
		this.isSpecial = false;
	}
	
	//Finder
	public static Finder<Integer, Product> find = new Finder<Integer, Product>(Integer.class, Product.class);
	
	// Setters for all the attributes that can be changed
		// in the editProduct.html page;
	/**
	 * Sets the name of the product;
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Sets the description of the product;
	 * @param desc
	 */
	public void setDesc(String desc) {
		this.description = desc;
	}
	
	public void setCondition(String condition){
		this.condition=condition;
	}

	
	/**
	 * Gets the information whether the product is sold or not;
	 * @return boolean isSold;
	 */
	public boolean isSold() {
		return isSold;
	}

	/**
	 * Sets the isSold boolean to either true or false 
	 * depending on whether the item is sold or not;
	 * @param isSold
	 */
	public void setSold(boolean isSold) {
		this.isSold = isSold;
	}
	
	/**
	 * If the product is bought returns the User who has bought the item;
	 * If not returns a null value;
	 * @return User buyer_user;
	 */
	public User getBuyerUser() {
		return buyerUser;
	}

	/**
	 * We use this method when this product has been bought, thus we
	 * set this buyer_user to the User who has bought the product;
	 * @param buyer_user
	 */
	public void setBuyerUser(User buyer_user) {
		this.buyerUser = buyer_user;
	}
	
	/**
	 * Getter for the Purchase Transaction of this product
	 * when the item is sold/bought;
	 * @return
	 */
	public TransactionP getPurchaseTransaction() {
		return purchaseTransaction;
	}

	/**
	 * Setter for the Purchase Transaction of this product
	 * when the item is sold/bought;
	 * @param purchaseTransaction
	 */
	public void setPurchaseTransaction(TransactionP purchaseTransaction) {
		this.purchaseTransaction = purchaseTransaction;
	}

	/**
	 * Sets the category of the product;
	 * @param mainCategory
	 */
	public void setCategory(MainCategory mainCategory) {
		this.mainCategory = mainCategory;
	}
	
	/**
	 * Sets the subCategory of the product;
	 * @param subCategory
	 */
	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	/**
	 * Sets the price of the product;
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Sets the availability of the product;
	 * @param availability
	 */
	public void setAvailability(String availability) {
		this.availability = availability;
	}
		
	/**
	 * @author Graca Nermin
	 * When the constructor is called. Meaning, when the item/product is published this date will be
	 * created as the publishedDate;
	 * @return publishedDate;
	 */
	public static String getDate() {
		Date date = Calendar.getInstance().getTime();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	/**
	 * Gets us the amount of bitpik credit used on this product;
	 * If not returns the 0;
	 * @return
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * Sets the amount of credit by the owner User;
	 * Used in the process of using the Credits 
	 * check CreditController.useCreditProcess();
	 * @param credit
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}

	/**
	 * Return the value, true or false depending on
	 * whether the product has been set to be special or not;
	 * Meaning, The product is special if someone has spent/used their credits
	 * on the products;
	 * @return
	 */
	public boolean isSpecial() {
		return isSpecial;
	}

	/**
	 * We set the true boolean for the isSpecial only when the
	 * owner User as spent and used their credits to use them
	 * on the product;
	 * @param isSpecial
	 */
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	
	/**
	 * This method is used to get the Statistics model of the (this)
	 * product;
	 * @return
	 */
	public Statistics getStatsProducts() {
		return statsProducts;
	}
	
	public String getCondition(){
		return condition;
	}

	/**
	 * This method is user to set the Statistics of the (this) product.
	 * @param statsProducts
	 */
	public void setStatsProducts(Statistics statsProducts) {
		this.statsProducts = statsProducts;
	}
	
	/**
	 * 
	 * @return formated price with two decimals
	 */
	public String getPriceString() {
		return String.format("%1.2f",price);
	}
	
	/**
	 * Converts the price entered in BAM currency
	 * and converts the amount to USD and formats 
	 * it into a string with two decimals;
	 * @return formated price with two decimals in Dollars
	 */
	public String getPriceinStringinUSD() {
		double priceInUSD = price * 0.56;
		return String.format("%1.2f",priceInUSD);
	}
	
	/**
	 * This method gets us the date of the date when the product
	 * should expire;
	 * @return
	 */
	public Date getExpirySpecial() {
		return expirySpecial;
	}

	/**
	 * We use this method to set the expiry Date of the product,
	 * till which it will be looked as a special product;
	 * @param expirySpecial
	 */
	public void setExpirySpecial(Date expirySpecial) {
		this.expirySpecial = expirySpecial;
	}
	
	/**
	 * Puts the ExpiryDate in the Format;
	 * @param c
	 * @return
	 */
	public String getExpiryDateInFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(this.expirySpecial.getTime());
	}
	
	/**
	 * Method creates a new product, by calling the constructor;
	 * @param name
	 * @param desc
	 * @param price
	 */
	public static Product create(String name, String desc, String longDesc, double price, User owner, MainCategory category, SubCategory subCategory, String availability,String condition) {
		Product newProduct = new Product(name, desc, longDesc,price, owner, category, subCategory, availability,condition);
		if(owner.isPikStore){
			newProduct.setSpecial(true);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 30); 
			newProduct.setExpirySpecial(c.getTime());
		}
		newProduct.statsProducts = new Statistics(newProduct);
		newProduct.save();
		return newProduct;
	}
	
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Method deletes Product which has id given to the method
	 * @param id = id of object Product which will be deleted from database
	 */
	public static void delete(int id) {
		  find.ref(id).delete();
	}
	
	public static String getIds(List<Product> products) {
		if(products.size() < 1){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		for (Product product : products) {
			sb.append(product.id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public static int[] getIdss(List<Product> products){//treba izbrisati 
		int []ids=new int[products.size()-1];
		int i=0;
		for(Product product : products){
			ids[i]=product.id;
			i++;
		}
		return ids;
		
	}

}
