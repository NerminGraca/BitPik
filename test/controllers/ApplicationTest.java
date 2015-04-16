package controllers;
import models.FAQ;
import models.MainCategory;
import models.Product;
import models.SubCategory;
import models.User;

import org.junit.*;

import play.test.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.assertNotNull;

public class ApplicationTest {
	
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}	
    /*
    @Test
	public void test() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						assertThat(browser.pageSource()).contains("BitPik");
						assertThat(browser.pageSource()).contains("Login");
						assertThat(browser.pageSource()).contains("Registracija");
						

					}
				});
	}
    
	@Test
	public void testRegistration() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/registration");
						browser.fill("#username").with("test");
						browser.fill("#password").with("testPass");
						browser.fill("#confirmPassword").with("testPass");
						browser.fill("#email").with("test@test.ba");
						browser.submit("#nameForm");
						User user = User.finder("test");
						user.verified = true;
						assertThat(browser.pageSource()).contains("BitPik");
						assertThat(browser.pageSource()).contains("mail");						
					}
				});
	}
	
	@Test
	public void testLogin() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						assertThat(browser.pageSource()).contains("BitPik");
						assertThat(browser.pageSource()).contains("Objavite oglas");
						assertThat(browser.pageSource()).contains("Korisnik");
						assertThat(browser.pageSource()).contains("Logout");
						
					}
				});
	}
	
	@Test
	public void testLogout() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/logout");
						assertThat(browser.pageSource()).contains("BitPik");
						assertThat(browser.pageSource()).contains("Login");
						assertThat(browser.pageSource()).contains("Registracija");
						
					}
				});
	}
	
	@Test
	public void testAdminPanelIfIsAdmin(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/adminPanel");
						assertThat(browser.pageSource()).contains("Korisnici");
						assertThat(browser.pageSource()).contains("Kategorije");
						assertThat(browser.pageSource()).contains("FAQS");
					}
		});
						
	}
	
	@Test
	public void testAdminPanelIfIsNotAdmin(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("selma");
						browser.fill("#password").with("selma");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/adminPanel");
						assertThat(browser.pageSource().contains("Kategorije"));
					}
		});
						
	}

	@Test
	public void testSearchExistingUser(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						browser.fill("#q").with("admin");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=admin");
						assertThat(browser.pageSource().contains("Korisnici:"));
						assertThat(browser.pageSource().contains("admin"));
						
					}
					
		});
	}
	
	@Test
	public void testSearchExistingProduct(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						User.createSaveUser("neko2", "12345", "neko2@gmail.com");
						User u = User.find(4);
						assertNotNull(u);
						MainCategory mc = MainCategory.findMainCategoryByName("Vozila");
						SubCategory sc = SubCategory.findSubCategoryByName("Automobili");
						Product.create("original_product2", "original_product_description","longDescription",
								10.00, u, mc, sc, "sarajevo");
						browser.fill("#q").with("original_product2");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=admin");
						assertThat(browser.pageSource().contains("Proizvodi:"));
						assertThat(browser.pageSource().contains("original_product2"));
						
					}
		});					
	}

	@Test
	public void testSearchNonExistingUser(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						browser.fill("#q").with("Selma");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=Selma");
						assertThat(browser.pageSource().contains("Korisnici:"));
						assertThat(browser.pageSource().contains("Nema rezultata za vasu pretragu"));
					}
						
	   });	
		
	}
	
	@Test
	public void testSearchNonExistingProduct(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						browser.fill("#q").with("Kuca");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=Kuca");
						assertThat(browser.pageSource().contains("Proizvodi:"));
						assertThat(browser.pageSource().contains("Nema rezultata za vasu pretragu"));
						
					}
		});	
	}
	//Ovaj test pada zbog ispisa u javaScriptu!
	@Test
	public void testSearchExistingFAQ(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						FAQ.create("pitanje", "odgovor");
						FAQ f = FAQ.finder(6);
						assertNotNull(f);
						browser.goTo("http://localhost:3333/faqs");
						browser.fill("#q").with("pitanje");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=pitanje");
						assertThat(browser.pageSource().contains("Rezultati pretrage:"));
						assertThat(browser.pageSource().contains("pitanje"));
						
					}
		});	
	}
	
	@Test
	public void testSearchNonExistingFAQ(){
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/faqs");
						browser.fill("#q").with("pitanje");
						browser.submit("#nameForm");
						browser.goTo("http://localhost:3333/search?q=pitanje");
						assertThat(browser.pageSource().contains("Rezultati pretrage:"));
						assertThat(browser.pageSource().contains("NEMA REZULTATA ZA VASU PRETRAGU"));
					}
		});
	}
	*/

}
