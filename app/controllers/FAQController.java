package controllers;

import java.util.List;

import models.FAQ;
import models.MainCategory;
import models.Product;
import models.User;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.addProduct;

public class FAQController extends Controller{

	static Form<FAQ> newFaq = new Form<FAQ>(FAQ.class);
	static Finder<Integer, FAQ> findFaq = new Finder<Integer, FAQ>(Integer.class, FAQ.class);
	static String usernameSes;
	static List<FAQ> FaqList = findFaq.all();
	
	public static Result addFaq() {
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		FAQ faq = FAQ.create(question, answer);
		User u = User.finder(usernameSes);
		
		return ok(showAllFaq.render(usernameSes, FaqList));	
	}
	
	public static Result showFaq(int id){
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		return ok(showFaq.render(usernameSes, faq));
	}
	
	public static Result showAllFaq(int id){
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		return ok(showAllFaq.render(usernameSes, FaqList));
	}
	
	public static Result editFaq(int id)
	{
		usernameSes = session("username");
		if (usernameSes == null) {
			usernameSes = "";
		}
		FAQ faq = FAQController.findFaq.byId(id);
		String question = newFaq.bindFromRequest().get().question;
		String answer = newFaq.bindFromRequest().get().answer;
		return redirect("/showFaq/" + faq);
	}

}
