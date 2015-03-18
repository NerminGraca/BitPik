package controllers;

import java.util.List;

import models.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.*;
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
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		
		FAQ faq = FAQ.create(question, answer);
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
			return redirect("/");
		}
		FAQ faq = FAQController.findFaq.byId(id);
		if(!currentUser.isAdmin)
		{
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
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		
		faq.setQuestion(question);
		faq.setAnswer(answer);
		faq.save();
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
			return redirect("/");
		}
		if(!currentUser.isAdmin)
		{
			return redirect("/");
		}
		FAQ.delete(id);
		return redirect(routes.FAQController.allFaqs());
	}
}
