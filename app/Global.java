import models.FAQ;
import models.MainCategory;
import models.User;
import play.Application;
import play.GlobalSettings;
import controllers.FAQController;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {

		if (User.finder("admin") == null) {
			// Administrator entry
			User ad = User
					.createAdmin("admin", "admin", "admin@admin.ba", true);
			ad.verified = true;
			ad.save();
		}

		if (MainCategory.findMainCategory(1) == null) {
			// Main Categories entry
			String categoryArray[] = { "Vozila", "Nekretnine",
					"Mobilni uređaji", "Kompjuteri", "Tehnika",
					"Nakit i satovi", "Moj dom", "Biznis i industrija",
					"Životinje", "Odjeća i obuća", "Ostale kategorije" };

			for (int i = 0; i < categoryArray.length; i++) {
				MainCategory.createMainCategory(categoryArray[i]);
			}
		}

		if (FAQController.findFaq.byId(1) == null) {
			// FAQ Entries;
			// the list of questions
			String[] questions = new String[] { 
					"Kako se registrovati?",
					"Kako objaviti proizvod?",
					"Kako obrisati proizvod?",
					"Kako editovati proizvod?",
					"Kako da vidim svoje podatke i gdje mogu vidjeti svoje proizvode?" };
			String[] answers = new String[] {
			// the list of adequate answers;
					"Kliknite na Registracija, nakon toga popunite formular sa vasim podacima i kliknite Registruj. Tad vam je proslijedjen mail sa konfirmacijskim linkom, kojeg kliknete, i time ste se registrovali.",
					"Nakon registracije, kliknete na dugme Objavite oglas. Zatim popunite podatke o vasem proizvodu kojeg objavljujete i kliknete na dugme Objavi. Te time ste uspjesno objavili proizvod.",
					"Delete product - description to be added",
					"Edit product - description to be added",
					"Nakon sto ste se registrovali i log-ovali na nas webiste, u gornjem desnom uglu ce biti prikazano dugme Korisnik i vas username. Kliknite na to dugme i to ce vas odvesti na stranicu sa vasim podacima i listom vasih objavljenih proizvoda."
					};
			for (int i = 0; i < questions.length; i++) {
				FAQ.create(questions[i], answers[i]);
			}
		}

	}
}