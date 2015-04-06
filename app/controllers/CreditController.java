package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.io.Files;

import helpers.*;
import models.*;
import play.i18n.Messages;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.*;
import views.html.*;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

/**
 * This controller is User to control the Users Credit
 * their amount, to get certain amount that User has,
 * to Use them when a User wants to use them on a a certain product
 * to get their product to be special(listed amoung the first ones) in search queries,
 * to subtract them when used on certain products;
 * Basically everything and every actions one with the Credits (which are simple integers),
 * is done with this class CreditControllers;
 * @author Necko
 *
 */
public class CreditController extends Controller{

	public static Form<BPCredit> creditForm = new Form<BPCredit>(BPCredit.class);
	
	/**
	 * Main page for the BitPikCredits of the User;
	 * On which we give the amount of the BitPikcredits the User has;
	 * and give him the option to either use the Credits he has on 
	 * certain products or an option to buy more Credits;
	 * @return Result mainCredits.html page;
	 */
	public static Result showCredits() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// 1.1 If the User is not Logged in;
		// 1.2 or if the User is an Admin; 
		// we redirect these kind of Users to the index.html page;
		if (currentUser == null || currentUser.isAdmin==true) {
			return redirect(routes.Application.index());
		}
		return ok(credits.render(currentUser));
		
	}
	
	/**
	 * This method shows the User the page, where he should enter
	 * in the form the amount of Credits that he wants to buy;
	 * @return
	 */
	public static Result addCredits() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// 1.1 If the User is not Logged in;
		// 1.2 or if the User is an Admin; 
		// we redirect these kind of Users to the index.html page;
		if (currentUser == null || currentUser.isAdmin==true) {
			return redirect(routes.Application.index());
		}
		return ok(addcredits.render(currentUser));
	}
	
	/**
	 * Transfers from the Credits to the amount in KM
	 * @param credit
	 * @return
	 */
	public static double creditToCost(int credit) {
		return (double)(credit*0.5);
	}
	
	/**
	 * Transfers the double amount from KM to 
	 * USD first then puts them in a String format;
	 * @param costKM
	 * @return
	 */
	public static String converterToStringUSD(double costKM) {
		double priceInUSD = costKM * 0.56;
		return String.format("%1.2f",priceInUSD);
	}
	
	/**
	 * Cost in Strings for printing;
	 * @return formated price with two decimals
	 */
	public static String getPriceString(double creditD) {
		return String.format("%1.2f",creditD);
	}
	
	
	/**
	 * Method should take the amount entered in the form
	 * as the amount of Credit that the User wants to buy' 
	 * @return
	 */
	public static Result addCreditsProcess() {
		int credit;
		try {
			credit = creditForm.bindFromRequest().get().credit;
			} catch(IllegalStateException e) {
			return redirect(routes.CreditController.showCredits());
			}
		double creditD = creditToCost(credit);
		String creditString = getPriceString(creditD);
		return ok(purchasecredit.render(creditString));
	
	}
	
	/**
	 * Starting of the paypal process for the purchase of the bitpik credits;
	 * In case that the User presses Cancel he is redirected to the page addcredits.html
	 * and in the case that the User goes through, he is redirected to the next page
	 * "/purchaseSuccessCredits/"html page with the cost of the credits in USD;
	 * @param priceKMString
	 * @return
	 */
	public static Result purchaseCredit(String priceKMString) {
		double priceCostKM = Double.parseDouble(priceKMString);
		String priceInUSD = converterToStringUSD(priceCostKM);
		// ovdje je vec PAyPal redirectanje dole sto se ispod ove metode nalazi... ne zaboravi conertovati u USD;
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		try{
			String accessToken = new OAuthTokenCredential("ARl5dVTUzOXK0p7O1KgG5ZpLg-E9OD5CgoqNXMuosC3efZWeZlBPODxDV6WeIFfJnS5atklHgrt8lMVO", 
					"EDrDunRMuM_aAbbILclme0f4dfL2kZ1OGrS8NVDIjWwN6N8G9s-vF0udi97t2rcP8_HiiGgkUL9XBhoS").getAccessToken();
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			Amount amount = new Amount();
			// We put the amount in USD and convert it to a String;
			amount.setTotal(priceInUSD);
			amount.setCurrency("USD");
			Transaction transaction = new Transaction();
			transaction.setDescription("Cestitamo, jos ste samo nekoliko koraka od kupovine  ' " + priceInUSD +
										" ' u dolarima iznosa kredita");
			transaction.setAmount(amount);
			
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);
			
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			
			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			RedirectUrls redirectUrls = new RedirectUrls();
			flash("buy_fail",  Messages.get("Paypal transakcija nije uspjela"));
			redirectUrls.setCancelUrl(UserController.OURHOST + "/addcredits");
			redirectUrls.setReturnUrl(UserController.OURHOST + "/purchaseSuccessCredits/"+priceInUSD);
			payment.setRedirectUrls(redirectUrls);
			Payment createdPayment = payment.create(apiContext);
			Logger.debug(createdPayment.toJSON());
			List<Links> links = createdPayment.getLinks();
			Iterator<Links> itr = links.iterator();
			while(itr.hasNext()){
				Links link = itr.next();
				if(link.getRel().equals("approval_url"))
				{
					return redirect(link.getHref());
				}
			}
			
		} catch(PayPalRESTException e)
		{
			Logger.warn(e.getMessage());
		}
		
		
		return TODO;


	}
	
	/**
	 * Successfull paypal process for the purchase of the bitpik credits;
	 * @param priceInUSD
	 * @return
	 */
	public static Result purchaseSuccessCredits(String priceInUSD) {
		DynamicForm paypalReturn = Form.form().bindFromRequest();
		String paymentId = paypalReturn.get("paymentId");
		String payerId = paypalReturn.get("PayerID");
		String token = paypalReturn.get("token");
				
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		try {
			String accessToken = new OAuthTokenCredential("ARl5dVTUzOXK0p7O1KgG5ZpLg-E9OD5CgoqNXMuosC3efZWeZlBPODxDV6WeIFfJnS5atklHgrt8lMVO", 
					"EDrDunRMuM_aAbbILclme0f4dfL2kZ1OGrS8NVDIjWwN6N8G9s-vF0udi97t2rcP8_HiiGgkUL9XBhoS").getAccessToken();
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			Payment payment = Payment.get(accessToken, paymentId);
			
			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(payerId);
			
			payment.execute(apiContext, paymentExecution);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return redirect(UserController.OURHOST + "/buycreditsuccess/"+priceInUSD);
	}
		
	
	/**
	 * After the paypal process of buying the bitpik credits;
	 * We set the value of the bitpik credits to the user
	 * adding them to the bitpik credits he has already had;
	 * @param priceInUSD
	 * @return
	 */
	public static Result buyCreditSuccess(String priceInUSD) {
		User buyerUser = SessionHelper.getCurrentUser(ctx());
		//1. No permission for unregistered user;
		if (buyerUser == null) {
			return redirect(routes.Application.index());
		}
		//2. No permission for an admin user;
		if (buyerUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		double priceinDB = Double.parseDouble(priceInUSD);
		double priceinKM = priceinDB / 0.56;
		double numCreditsinD = priceinKM * 2;
		//New amount of credits bought; (so that the rounding to the int works)
		int numCredits = (int)Math.floor(numCreditsinD + 0.5);
		// The amount of credits that the User already has; 
		int currentCredits = buyerUser.getCredits().getCredit();
		// New overall updated amount of credits for the new user;
		int newNumCredits = numCredits + currentCredits;
		buyerUser.bpcredit.setCredit(newNumCredits);
		buyerUser.save();
	//	if later we choose to archive everytransaction for every bitpik bought
	//  TransactionP temp = new TransactionP(token, p); *(+token to be sent here; product attribute?)
		flash("buy_credit_success", Messages.get("Cestitamo, Uspjesno ste kupili BitPik Kredite"));
		return ok(credits.render(buyerUser));
	}
	
	// Halftime - now the using of the credits;
	
	/**
	 * The GET redirection for the page where the user enters the amount
	 * of bitpik credits he wants to use;
	 * @param id
	 * @return
	 */
	public static Result useCredits( int id) {
		Product p = Product.find.byId(id);
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// 1.1 If the User is not Logged in;
		// 1.2 or if the User is an Admin; 
		// we redirect these kind of Users to the index.html page;
		if (currentUser == null || currentUser.isAdmin==true) {
			return redirect(routes.Application.index());
		}
		return ok(makespecialproduct.render(p));
	}
	
	/**
	 * After entering the amount of credits to be used by the user
	 * we take the amounts entered here with the POST method;
	 * And add them to the product the User has choosen,
	 * And as well make the product special ("izdvojen oglas");
	 * @param id
	 * @return
	 */
	public static Result useCreditsProcess(int id) {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		int credit;
		try {
			credit = creditForm.bindFromRequest().get().credit;
		} catch(IllegalStateException e) {
			return redirect(routes.CreditController.showCredits());
		}
		int oldAmount = currentUser.bpcredit.getCredit();
		// If the User wants to use more credits than he actually has we redirect him;
		if (credit > oldAmount) {
			flash("use_credit_poor", Messages.get("Zao nam je, Nemate dovoljno kredita. Molim vas da kupite jos kredita"));
			return redirect(routes.CreditController.showCredits());
		}
		
		int newAmount = oldAmount - credit;
		// We set the amount of credit to the new amount, after subtracting the used credits from the old amount
		currentUser.bpcredit.setCredit(newAmount);
		currentUser.save();
		Product p = Product.find.byId(id);
		p.setCredit(credit);
		p.setSpecial(true);
		p.save();
	
		flash("use_credit_success", Messages.get("Cestitamo, Uspjesno ste izdvojili oglas i iskoristili BitPik Kredite"));
		List <Product> l = ProductController.findProduct.where().eq("owner.username", currentUser.username).eq("isSold", false).findList();
		return ok(profile.render(l, currentUser));
		
	}
	
	/**
	 * Redirecting to the page where the user enters the amount of Credits 
	 * he wants to update to the artical that is already made special (izdvojen);
	 * @return
	 */
	public static Result updateCredits(int id) {
		Product p = Product.find.byId(id);
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// 1.1 If the User is not Logged in;
		// 1.2 or if the User is an Admin; 
		// we redirect these kind of Users to the index.html page;
		if (currentUser == null || currentUser.isAdmin==true) {
			return redirect(routes.Application.index());
		}
		return ok(updatecredits.render(p));
	
	}
	
	/**
	 * Updating the amount of the credits that we add to the product;
	 * As the method of using the credits on a product for the first time
	 * this method as well sets the users bpcredits to the new amount 
	 * subtracting the used amount from the old amount;
	 * And now we set the products credit to the new amount which is the previous
	 * amount of credits that the product had plus the new amount we had just updated for; 
	 * @param id
	 * @return
	 */
	public static Result updateCreditsProcess(int id) {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		int credit;
		try {
			credit = creditForm.bindFromRequest().get().credit;
		} catch(IllegalStateException e) {
			return redirect(routes.CreditController.showCredits());
		}
		int oldAmount = currentUser.bpcredit.getCredit();
		// If the User wants to update more credits than he actually has we redirect him;
		if (credit > oldAmount) {
			flash("update_credit_poor", Messages.get("Zao nam je, Nemate dovoljno kredita. Molim vas da kupite jos kredita"));
			return redirect(routes.CreditController.showCredits());
		}
		
		int newAmount = oldAmount - credit;
		// We set the amount of credit to the new amount, after subtracting the used credits from the old amount
		currentUser.bpcredit.setCredit(newAmount);
		currentUser.save();
		Product p = Product.find.byId(id);
		int previousAmountP = p.getCredit();
		int newAmountProduct = previousAmountP + credit;
		p.setCredit(newAmountProduct);
		p.save();
		flash("update_credit_success", Messages.get("Cestitamo, Uspjesno ste dopunili BitPik Kredite na oglasu"));
		
		List <Product> l = ProductController.findProduct.where().eq("owner.username", currentUser.username).eq("isSold", false).findList();
		return ok(profile.render(l, currentUser));
	}
}
