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
		
	public static Result addFaq() {
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (usernameSes == null) {
			usernameSes = "";
		}
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		List <FAQ> faqList = findFaq.all();
		FAQ faq = FAQ.create(question, answer);
		return ok(faqs.render(usernameSes, faqList, currentUser));	
	}
	
	public static Result allFaqs(){
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		List <FAQ> faqList = findFaq.all();
		if (currentUser == null) {
			usernameSes = "";
		}
		return ok(faqs.render(usernameSes, faqList, currentUser));
				
	}
	
	public static Result editFaq(int id)
	{
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		return ok(editFaq.render(usernameSes, faq, currentUser));
	}
	
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
	
	public static Result deleteFaq(int id) {		
		  FAQ.delete(id);
		  return redirect(routes.FAQController.allFaqs());
	}
}
