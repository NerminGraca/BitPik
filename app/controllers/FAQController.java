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

		FAQ faq = FAQ.create(question, answer);
		List <FAQ> faqList = findFaq.all();
		return ok(faqs.render(usernameSes, faqList, currentUser));	
	}
	/*
	public static Result showFaq(int id){
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		return TODO;
		//return ok(showFaq.render(usernameSes, faq));
	}
	*/
	public static Result allFaqs(){
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			usernameSes = "";
		}
		List <FAQ> faqList = findFaq.all();
		return ok(faqs.render(usernameSes, faqList, currentUser));
				
	}
	/*
	public static Result editFaq(int id)
	{
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		return TODO;
		//return redirect("/showFaq/" + faq);
	}
*/
}
