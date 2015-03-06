import models.MainCategory;
import models.User;
import play.Application;
import play.GlobalSettings;
import controllers.UserController;

public class Global extends GlobalSettings {
	
	@Override
	public void onStart(Application app) {

		//Administrator entry
		User ad = User.createAdmin("admin", "admin", "admin@admin.ba", true);	
		UserController.insertAdmin(ad.username);
		
		//Main Categories entry
		String categoryArray[] = {"Vozila", "Nekretnine", "Mobilni uređaji", 
				"Kompjuteri", "Tehnika", "Nakit i satovi", "Moj dom", "Biznis i industrija",
				"Životinje", "Odjeća i obuća", "Ostale kategorije"};
		
		for (int i = 0; i < categoryArray.length; i++) {
			MainCategory.createMainCategory(categoryArray[i]);
		}
	}
}