import models.FAQ;
import models.MainCategory;
import models.User;
import play.Application;
import play.GlobalSettings;
import controllers.FAQController;
import controllers.UserController;

public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {
		
		if (User.finder("admin") == null) {
			//Administrator entry
			User ad = User.createAdmin("admin", "admin", "admin@admin.ba", true);	
			ad.verified = true;
			ad.save();
		}
		
		if (MainCategory.findMainCategory(1) == null) {
			//Main Categories entry
			String categoryArray[] = {"Vozila", "Nekretnine", "Mobilni uređaji", 
					"Kompjuteri", "Tehnika", "Nakit i satovi", "Moj dom", "Biznis i industrija",
					"Životinje", "Odjeća i obuća", "Ostale kategorije"};
			
			for (int i = 0; i < categoryArray.length; i++) {
				MainCategory.createMainCategory(categoryArray[i]);
			}
		}	
		
		if (FAQController.findFaq.byId(1) == null) {
			//FAQ Entries;
			String[] questions = new String[]{"How to register?", "How to publish a product?", "How to delete a product?"};
			String[] answers = new String[]{"Registracija", "Objavite proizvod", "Delete product"};
			for (int i = 0; i < questions.length; i++) {
				FAQ.create(questions[i], answers[i]);
			}
		}
		
	}
}