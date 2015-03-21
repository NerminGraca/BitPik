package controllers;

import helpers.SessionHelper;

import java.util.List;

import models.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import play.*;
import views.html.*;

public class FAQController extends Controller{

	public static Form<FAQ> newFaq = new Form<FAQ>(FAQ.class);
	public static Finder<Integer, FAQ> findFaq = new Finder<Integer, FAQ>(Integer.class, FAQ.class);
	static String usernameSes;
		
	/**
	 * Method adds a new FAQ to the list
	 * @return Result
	 */
	
	public static Result addFaq() {
		
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (usernameSes == null) {
			usernameSes = "";
		}		
		String question;
		String answer;
		
		try {
			question = newFaq.bindFromRequest().get().question;
			answer = newFaq.bindFromRequest().get().answer;
		} catch(IllegalStateException e) {
			return redirect(routes.FAQController.allFaqs());
		}
		
		FAQ faq = FAQ.create(question, answer);
		Logger.of("faq").info("Admin added a new FAQ");
		List <FAQ> faqList = findFaq.all();
		return ok(faqs.render(usernameSes, faqList, currentUser));	
	}
	
	/**
	 * Method shows a list of all FAQs, visible to all users, registered or not
	 * @return Result
	 */
	
	public static Result allFaqs(){
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		List <FAQ> faqList = findFaq.all();
		if (currentUser == null) {
			usernameSes = "";
		}
		return ok(faqs.render(usernameSes, faqList, currentUser));
				
	}
	
	/**
	 * Method is used for editing FAQs, questions or answers.
	 * This method is available only to admin
	 * @param id
	 * @return Result
	 */
	
	public static Result editFaq(int id)
	{
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			Logger.of("faq").warn("Not registered user tried to edit a FAQ");
			return redirect("/");
		}
		FAQ faq = FAQController.findFaq.byId(id);
		if(!currentUser.isAdmin)
		{
			Logger.of("faq").warn("Not an admin user tried to edit a FAQ");
			return redirect("/");
		}
		return ok(editFaq.render(usernameSes, faq, currentUser));
	}
	
	/**
	 * Method that saves edited FAQ
	 * @param id
	 * @return Result
	 */
	
	public static Result saveEditedFaq(int id)
	{
		FAQ faq = FAQController.findFaq.byId(id);
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			usernameSes = "";
		}
		String question;
		String answer;
		try {
			question = newFaq.bindFromRequest().get().question;
			answer = newFaq.bindFromRequest().get().answer;
		} catch(IllegalStateException e) {
			return ok(editFaq.render(usernameSes, faq, currentUser));
		}
		
		faq.setQuestion(question);
		faq.setAnswer(answer);
		faq.save();
		Logger.of("faq").info("Admin user updated a FAQ");
		return redirect(routes.FAQController.allFaqs());
	}
	
	/**
	 * Method that deletes FAQ.
	 * This method is available only to admin
	 * @param id
	 * @return Result
	 */
	
	public static Result deleteFaq(int id) {
	
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			Logger.of("faq").warn("Not registered user tried delete a FAQ");
			return redirect("/");
		}
		if(!currentUser.isAdmin)
		{
			Logger.of("faq").warn("Not an admin user tried delete a FAQ");
			return redirect("/");
		}
		FAQ.delete(id);
		Logger.of("faq").info("Admin deleted a FAQ");
		return redirect(routes.FAQController.allFaqs());
	}
}
