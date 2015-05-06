package controllers;

import helpers.SessionHelper;

import java.util.List;

import models.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.i18n.Messages;
import play.mvc.Result;
import play.*;
import views.html.*;

/**
 * Class FAQController which contains all the handling of all the actions and
 * function that are done for the FAQs on our portal;
 *
 */
public class FAQController extends Controller{

	public static Form<FAQ> newFaq = new Form<FAQ>(FAQ.class);
	public static Finder<Integer, FAQ> findFaq = new Finder<Integer, FAQ>(Integer.class, FAQ.class);
	static String usernameSes;
		
	/**
	 * Method adds a new FAQ to the list
	 * 
	 * @return Result renders the faqs page;
	 */
	public static Result addFaq() {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		
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
			flash("add_faq_null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
			return redirect(routes.FAQController.allFaqs());
		}
		
		FAQ.create(question, answer);
		Logger.of("faq").info("Admin added a new FAQ");
		List <FAQ> faqList = findFaq.all();
		flash("add_faq_success", Messages.get("Uspješno ste dodali novi FAQ."));
		return ok(faqs.render(usernameSes, faqList, currentUser));	
	}
	
	/**
	 * Method shows a list of all FAQs, visible to all users, registered or not
	 * 
	 * @return Result Renders the faqs page;
	 */
	public static Result allFaqs(){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		List <FAQ> faqList = findFaq.all();
		if (currentUser == null) {
			usernameSes = "";
		}
		return ok(faqs.render(usernameSes, faqList, currentUser));
				
	}

	/**
	 * Method is used for editing FAQs, questions or answers. This method is
	 * available only to admin
	 * 
	 * @param id
	 * @return Result renders the editFaq page;
	 */
	public static Result editFaq(int id)
	{
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		usernameSes = session("username");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			Logger.of("faq").warn("Not registered user tried to edit a FAQ");
			return redirect(routes.Application.index());
		}
		FAQ faq = FAQController.findFaq.byId(id);
		if(!currentUser.isAdmin)
		{
			Logger.of("faq").warn("Not an admin user tried to edit a FAQ");
			return redirect(routes.Application.index());
		}
		return ok(editFaq.render(usernameSes, faq, currentUser));
	}
	
	/**
	 * Method that saves edited FAQ
	 * 
	 * @param id
	 * @return Result rendering the faqs page or the editFaq page;
	 */
	public static Result saveEditedFaq(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
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
			flash("edit_faq__null_field", Messages.get("Molimo Vas popunite sva polja u formi."));
			return ok(editFaq.render(usernameSes, faq, currentUser));
		}
		
		faq.setQuestion(question);
		faq.setAnswer(answer);
		faq.save();
		Logger.of("faq").info("Admin user updated a FAQ");
		flash("edit_faq_success", Messages.get("Uspješno ste izmijenili FAQ."));
		return redirect(routes.FAQController.allFaqs());
	}
	
	/**
	 * Method that deletes FAQ. This method is available only to admin
	 * 
	 * @param id
	 * @return Result redirects the user to the faqs page;
	 */
	public static Result deleteFaq(int id) {
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
	
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if (currentUser == null) {
			Logger.of("faq").warn("Not registered user tried delete a FAQ");
			return redirect(routes.Application.index());
		}
		if(!currentUser.isAdmin)
		{
			Logger.of("faq").warn("Not an admin user tried delete a FAQ");
			return redirect(routes.Application.index());
		}
		FAQ.delete(id);
		Logger.of("faq").info("Admin deleted a FAQ");
		flash("delete_faq_success",  Messages.get("Uspješno ste izbrisali FAQ."));
		return redirect(routes.FAQController.allFaqs());
	}
	
	/**
	 * Method searchFaq(String q) receives the String q and by the same String
	 * search through all the FAQs
	 * 
	 * @param q
	 * @return Renders the listaFAQs with the faqs found and the currentUser
	 *         that is logged in;
	 */
	public static Result searchFaq(String q){
		User u = SessionHelper.getCurrentUser(ctx());
		List<Blogger> bloggerList = Blogger.find.all();
		if(u != null && u.username.equals("blogger")){
			return ok(blog.render(bloggerList,u));
		}
		Logger.debug("seacrh");
		User currentUser = SessionHelper.getCurrentUser(ctx());
		List<FAQ>faqs=FAQ.find.where("UPPER(question) LIKE UPPER('%"+q+"%')").findList();
		Logger.debug(""+faqs.size());
		
		return ok(listaFAQs.render(faqs,currentUser));		
	}
}
