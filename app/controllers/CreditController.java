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
	
	/*
	public static Result purchaseProcessingCredits(int id) {
		Product p = Product.find.byId(id);
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		try{
			String accessToken = new OAuthTokenCredential("ARl5dVTUzOXK0p7O1KgG5ZpLg-E9OD5CgoqNXMuosC3efZWeZlBPODxDV6WeIFfJnS5atklHgrt8lMVO", 
					"EDrDunRMuM_aAbbILclme0f4dfL2kZ1OGrS8NVDIjWwN6N8G9s-vF0udi97t2rcP8_HiiGgkUL9XBhoS").getAccessToken();
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			Amount amount = new Amount();
			// We put the amount in USD and convert it to a String;
			amount.setTotal(p.getPriceinStringinUSD());
			amount.setCurrency("USD");
			Transaction transaction = new Transaction();
			transaction.setDescription("Cestitamo, jos ste samo nekoliko koraka od kupovine proizvoda '" + p.name +
										"' sa slijedecim opisom : '" + p.description + "'");
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
			redirectUrls.setCancelUrl(OURHOST + "/showProduct/"+ id);
			redirectUrls.setReturnUrl(OURHOST + "/purchasesuccess/"+id);
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
	*/
	
	/*
	public static Result purchaseSuccessCredits(int id) {
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
		return redirect(OURHOST + "/buyingAProduct/"+id+"/"+token);
	}
		*/
	
	/*
	public static Result buyCreditSuccess(int amountCredit, String token) {
		User buyerUser = SessionHelper.getCurrentUser(ctx());
		//1. No permission for unregistered user;
		if (buyerUser == null) {
			return redirect(routes.Application.index());
		}
		//2. No permission for an admin user;
		if (buyerUser.isAdmin) {
			return redirect(routes.Application.index());
		}
		Product p = findProduct.byId(product_id);
		//3. URL Security - No Product under the given id number;
		if (p == null) {
			return redirect(routes.Application.index());
		}
		// URL Security;
		//4. No permission for the user to buy his own product (BE Security);
		// Although we will hide the "KUPI"/"BUY" button from the user for
		// his own products on certain .html pages; with listing of products;
				
		if (buyerUser == p.owner) {
			return redirect(routes.Application.index());
		}
		TransactionP temp = new TransactionP(token, p);
		p.setPurchaseTransaction(temp);
		p.setSold(true);
		p.setBuyerUser(buyerUser);
		buyerUser.bought_products.add(p);
		p.save();
		List <Product> l = ProductController.findProduct.where().eq("owner.username", buyerUser.username).eq("isSold", false).findList();
		Logger.of("product").info("User "+ buyerUser.username +" bought the product '" + p.name + "'");
		flash("buy_product_success", Messages.get("Cestitamo, Uspjesno ste kupili proizvod...Proizvod pogledajte pod KUPLJENI PROIZVODI!"));
		return ok(profile.render(l, buyerUser));
	}*/
}
