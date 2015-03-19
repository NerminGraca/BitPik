import models.FAQ;
import models.MainCategory;
import models.SubCategory;
import models.User;
import play.Application;
import play.GlobalSettings;
import controllers.FAQController;
import play.Logger;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		Logger.info("Application start");
		if (User.finder("admin") == null) {
			// Administrator entry
			User ad = User
					.createAdmin("admin", "admin", "admin@admin.ba", true);
			ad.verified = true;
			ad.save();
		}

		if (MainCategory.findMainCategory(1) == null) {
			//Main Categories entry
			String[] categoryArray = {"Vozila", "Odjeca i obuca", "Kompjuteri", "Biznis i industrija",
					"Mobilni uredjaji", "Moj dom", "Nekretnine", "Literatura", "Tehnika", "Sportska oprema",
					"Nakit i satovi", "Kolekcionarstvo", "Ljepota i zdravlje", "Zivotinje", "Video igre"};
			
			for (int i = 0; i < categoryArray.length; i++) {
				MainCategory.createMainCategory(categoryArray[i]);
			}
			
			String[] vozilaSubCats = {"Dijelovi i oprema", "Automobili", "Motocikli", "Bicikli",
					"Teretna vozila", "Prikolice", "Nautika", "Autobusi i minibusi", "Kamperi"};
			MainCategory mc1 = MainCategory.findMainCategory(1);
			for (int i = 0; i < vozilaSubCats.length; i++) {
				SubCategory.createSubCategory(vozilaSubCats[i], mc1);
			}
			
			String[] odjecaSubCats = {"Zene", "Muskarci", "Djevojcice", "Djecaci", "Unisex", "Ostalo"};
			MainCategory mc2 = MainCategory.findMainCategory(2);
			for (int i = 0; i < odjecaSubCats.length; i++) {
				SubCategory.createSubCategory(odjecaSubCats[i], mc2);
			}
			
			String[] kompSubCats = {"Kompjuterska oprema", "Laptopi", "Desktop racunari", "Mreze i Komunikacije",
					"Softver", "Serveri", "Ostalo"};
			MainCategory mc3 = MainCategory.findMainCategory(3);
			for (int i = 0; i < kompSubCats.length; i++) {
				SubCategory.createSubCategory(kompSubCats[i], mc3);
			}
			
			String[] biznisSubCats = {"Masine i alati", "Poljoprivreda", "Gradevinarstvo", "Ugostiteljska oprema",
					"Servisi i usluge", "Trgovacka oprema", "Materijali, dijelovi i oprema", "Kancelarijski materijal",
					"Biznis i web stranice", "Oprema za zastitu na radu", "Ostalo"};
			MainCategory mc4 = MainCategory.findMainCategory(4);
			for (int i = 0; i < biznisSubCats.length; i++) {
				SubCategory.createSubCategory(biznisSubCats[i], mc4);
			}
			
			String[] mobitelSubCats = {"Mobiteli", "Dijelovi i oprema", "Tablet PCs", "Bluetooth uredjaji",
					"PDA i Pocket PCs", "Smartwatch (pametni satovi)", "Ostalo" };
			MainCategory mc5 = MainCategory.findMainCategory(5);
			for (int i = 0; i < mobitelSubCats.length; i++) {
				SubCategory.createSubCategory(mobitelSubCats[i], mc5);
			}
			
			String[] domSubCats = {"Namjestaj", "Vrt biljke i vrtlarstvo", "Grijanje i hladjenje", "Dekoracije",
					"Prehrana i pica", "Stolarija", "Lampe i rasvjeta", "Kuhinja", "Sigurnosni uredjaji", "Kupatilo",
					"Cilimi, tepisi, etisoni i ostalo", "Kupatilo", "Sredstva i uredjaji za ciscenje", "Tekstilni Proizvodi",
					"Vodoistalacije i oprema", "Ostalo"};
			MainCategory mc6 = MainCategory.findMainCategory(6);
			for (int i = 0; i < domSubCats.length; i++) {
				SubCategory.createSubCategory(domSubCats[i], mc6);
			}
			
			String[] nekretnineSubCats = {"Stanovi", "Kuce", "Zemljista", "Poslovni prostori", "Vikendice",
					"Apartmani", "Sobe", "Garaze", "Turisticki smjestaj", "Montazni objekti", "Ostalo"};
			MainCategory mc7 = MainCategory.findMainCategory(7);
			for (int i = 0; i < nekretnineSubCats.length; i++) {
				SubCategory.createSubCategory(nekretnineSubCats[i], mc7);
			}
			
			String[] literaturaSubCats = {"Knjige", "Stripovi", "E-Knjige", "Magazini", "Skolski pribor",
					"Audioknjige", "Katalozi", "Ostalo"};
			MainCategory mc8 = MainCategory.findMainCategory(8);
			for (int i = 0; i < literaturaSubCats.length; i++) {
				SubCategory.createSubCategory(literaturaSubCats[i], mc8);
			}
			
			String[] tehnikaSubCats = {"Kamere i foto aparati", "Bijela tehnika", "TV (Televizori)", "Multimedija",
					"Mjerno-regulacioni instrumenti", "Medicinska oprema", "Satelitska oprema", "Video iPod i mp3 playeri",
					"Telekomunikacije", "Elektromaterijal", "Radio i walkman uredjaji", "Kucna kina", "Radio amaterski uredjaji",
					"DVD/Blu-ray player", "Ostalo"};
			MainCategory mc9 = MainCategory.findMainCategory(9);
			for (int i = 0; i < tehnikaSubCats.length; i++) {
				SubCategory.createSubCategory(tehnikaSubCats[i], mc9);
			}
			
			String[] sportSubCats = {"Vanjski sportovi", "Timski sportovi", "Fitness i trening", "Zimski sportovi",
					"Borilacki sportovi", "Tenis", "Palninarenje i alpinizam", "Vodeni sportovi", "Ekstremni sportovi", "Ostalo"};
			MainCategory mc10 = MainCategory.findMainCategory(10);
			for (int i = 0; i < sportSubCats.length; i++) {
				SubCategory.createSubCategory(sportSubCats[i], mc10);
			}
			
			String[] nakitSubCats = {"Rucni satovi", "Lancici i ogrlice", "Nausnice", "Narukvice", "Prstenje", "Zlato i srebro",
					"Repromaterijal", "Privjesci", "Piercing nakit", "Dzepni satovi", "Ostalo"};
			MainCategory mc11 = MainCategory.findMainCategory(11);
			for (int i = 0; i < nakitSubCats.length; i++) {
				SubCategory.createSubCategory(nakitSubCats[i], mc11);
			}
			
			String[] kolekcSubCats = {"Numizmatika", "Militarija", "Znacke", "Filatelija", "Slicice i albumi", "Maketarstvo/modelarstvo",
					"Suveniri", "Razglednice", "Stare ulaznice", "Zastave", "Figurice", "Religija", "Salvete", "Telefonske kartice", "Ostalo"};
			MainCategory mc12 = MainCategory.findMainCategory(12);
			for (int i = 0; i < kolekcSubCats.length; i++) {
				SubCategory.createSubCategory(kolekcSubCats[i], mc12);
			}
			
			String[] ljepotaSubCats = {"Parfemi", "Njega noktiju", "Zdravlje", "Koza i tijelo", "Sminka", "Njega kose i brade",
					"Naocale", "Oralna higijena", "Ostalo"};
			MainCategory mc13 = MainCategory.findMainCategory(13);
			for (int i = 0; i < ljepotaSubCats.length; i++) {
				SubCategory.createSubCategory(ljepotaSubCats[i], mc13);
			}
			
			String[] zivotinjeSubCats = {"Kucni ljubimci", "Domace zivotinje", "Oprema", "Ostalo"};
			MainCategory mc14 = MainCategory.findMainCategory(14);
			for (int i = 0; i < zivotinjeSubCats.length; i++) {
				SubCategory.createSubCategory(zivotinjeSubCats[i], mc14);
			}
			
			String[] videoIgreSubCats = {"Igre", "Konzole", "Dijelovi i oprema", "Internet/browser igre", "Ostalo"};
			MainCategory mc15 = MainCategory.findMainCategory(15);
			for (int i = 0; i < videoIgreSubCats.length; i++) {
				SubCategory.createSubCategory(videoIgreSubCats[i], mc15);
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