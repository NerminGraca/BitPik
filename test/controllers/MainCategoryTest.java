package controllers;

import models.MainCategory;
import models.Product;
import models.User;

import org.junit.Test;

import play.test.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class MainCategoryTest extends WithApplication {
	
	@Test
	public void testMainCategories() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333");
						assertThat(browser.pageSource()).contains("Vozila");
					}
				});
	}
	
	@Test
	public void testCategories() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User owner = User.finder("admin");
						MainCategory mc = MainCategory.findMainCategory(1);
						Product.create("Auto", "New", 5000, owner, mc, mc.subCategories.get(1), "Svugdje");
						browser.goTo("http://localhost:3333/kategorija/1");
						assertThat(browser.pageSource()).contains("Vozila");
						assertThat(browser.pageSource()).contains("Auto");
					}
				});
	}
	
	@Test
	public void testAllCategoryNoUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/listaKategorija");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni");
						assertThat(browser.pageSource()).doesNotContain("Izbriši");
					}
				});
	}
	
	@Test
	public void testAllCategoryRegularUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User testUser = User.create("tester", "tester", "test@test.ba");
						testUser.verified = true;
						
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("tester");
						browser.fill("#password").with("tester");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaKategorija");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni");
						assertThat(browser.pageSource()).doesNotContain("Izbriši");
					}
				});
	}
	
	@Test
	public void testAllCategoryAdmin() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaKategorija");
						assertThat(browser.pageSource()).contains("Vozila");
						assertThat(browser.pageSource()).contains("Izmijeni");
						assertThat(browser.pageSource()).contains("Izbrisi");
					}
				});
	}
	
	@Test
	public void testEditCategoryNoUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/editMainCategory/1");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni ime");
					}
				});
	}
	
	@Test
	public void testEditCategoryRegularUser() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User testUser = User.create("tester", "tester", "test@test.ba");
						testUser.verified = true;
						
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("tester");
						browser.fill("#password").with("tester");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editMainCategory/1");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni ime");
					}
				});
	}
	
	@Test
	public void testEditCategoryAdmin() {		
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editMainCategory/1");
						assertThat(browser.pageSource()).contains("Izmijeni ime");
						assertThat(browser.pageSource()).contains("Spasi");
					}
				});
	}
	
	@Test
	public void testAddMainCategory() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaKategorija");
						browser.fill("#addCategoryFill").with("JavaProgremeri");
						browser.submit("#productForm");
						assertThat(browser.pageSource()).contains("JavaProgremeri");
					}
				});
	}
	
	@Test
	public void testSaveEditMainCategory() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/editMainCategory/1");
						browser.fill("#name").with("Vozilice");
						browser.submit("#productForm");
						assertThat(browser.pageSource()).contains("Vozilice");
						
					}
		});
	}
	/*
	@Test
	public void testDeleteMainCategory() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						browser.goTo("http://localhost:3333/listaKategorija");
						assertThat(browser.pageSource()).contains("Vozila");
						browser.submit("Izbrisi");
						assertThat(browser.pageSource()).doesNotContain("Vozila");
					}
		});
	}*/
}
