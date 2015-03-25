package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import models.MainCategory;
import models.Product;
import models.SubCategory;
import models.User;

import org.junit.Test;

import play.libs.F.Callback;
import play.test.TestBrowser;
import play.test.WithApplication;

public class SubCategoryTest extends WithApplication {
	/*
	@Test
	public void testSubcategoriesAdmin() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaPodKategorija/1");
						assertThat(browser.pageSource()).contains("Automobili");
					}
				});
	}
	
	@Test
	public void testSubcategoriesRegUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User testUser = User.create("tester", "tester", "test@test.ba");
						testUser.verified = true;
						
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("tester");
						browser.fill("#password").with("tester");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaPodKategorija/1");
						assertThat(browser.pageSource()).doesNotContain("Automobili");
					}
				});
	}
	
	@Test
	public void testSubcategoriesNoUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						
						browser.goTo("http://localhost:3333/listaPodKategorija/1");
						assertThat(browser.pageSource()).doesNotContain("Automobili");
					}
				});
	}

	@Test
	public void testEditSubCategoryNoUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/editSubCategory/1");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni ime");
					}
				});
	}
	
	@Test
	public void testEditSubCategoryRegularUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User testUser = User.create("tester", "tester", "test@test.ba");
						testUser.verified = true;
						
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("tester");
						browser.fill("#password").with("tester");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editSubCategory/1");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni ime");
					}
				});
	}
	
	@Test
	public void testEditSubCategoryAdmin() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editSubCategory/2");
						assertThat(browser.pageSource()).contains("Izmijeni ime");
						assertThat(browser.pageSource()).contains("Automobili");
					}
				});
	}
	
	@Test
	public void testSaveEditSubCategory() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editSubCategory/1");
						browser.fill("#name").with("Vozilice");
						browser.submit("#productForm");
						browser.goTo("http://localhost:3333/listaPodKategorija/1");
						assertThat(browser.pageSource()).contains("Vozilice");
						
					}
		});
	}
	
	@Test
	public void testDeleteSubCategory() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaPodKategorija/1");
						assertThat(browser.pageSource()).contains("Automobili");
						browser.goTo("http://localhost:3333/listaPodKategorija/2/delete");
						assertThat(browser.pageSource()).doesNotContain("Automobili");
					}
		});
	}
	
	@Test
	public void testSubCategoryView() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User.createSaveUser("neko2", "12345", "neko2@gmail.com");
						User u = User.find(4);
						assertNotNull(u);
						// String name, String desc, double price, User owner, MainCategory
						// category, String availability
						MainCategory mc = MainCategory.findMainCategoryByName("Vozila");
						SubCategory sc = SubCategory.findSubCategoryByName("Automobili");
						Product.create("golf", "mk2",
								1000.00, u, mc, sc, "sarajevo");
						
						browser.goTo("http://localhost:3333/kategorija/1");
						assertThat(browser.pageSource()).contains("golf");
						browser.goTo("http://localhost:3333/podKategorija/2");
						assertThat(browser.pageSource()).contains("golf");
						browser.goTo("http://localhost:3333/podKategorija/1");
						assertThat(browser.pageSource()).doesNotContain("golf");
					}
		});
	}*/
}
