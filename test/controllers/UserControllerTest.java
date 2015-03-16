package controllers;

import helpers.HashHelper;

import java.util.List;

import models.*;

import org.junit.*;

import play.mvc.*;
import controllers.UserController;
import play.test.TestBrowser;
import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import models.User;
import play.test.*;
import play.libs.F.*;
import static org.fest.assertions.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest extends WithApplication{
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}

	/**
	 * Test for : Going on to the webpage "/profile";
	 * while logged in as a User with the username : "necko";
	 *//*
	@Test
	public void testProfileUser() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						// LogIn
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
						//Creating a product
						//takes the new attributes that are entered in the "form" and saves;
						MainCategory mc = MainCategory.findMainCategoryByName("Kompjuteri");
						Product.create("original_product", "original_product_description", 10.00, u, mc, "sarajevo");
						Product p = Product.find.byId(1);
						assertNotNull(p);
						// Going to the profile page; (checking for data of the user, some Strings and product data;)
						browser.goTo("http://localhost:3333/profile");
						assertThat(browser.pageSource()).contains("necko");
						assertThat(browser.pageSource()).contains("necko@test.com");
						assertThat(browser.pageSource()).contains("Izmijeni");
						assertThat(browser.pageSource()).contains("Ne");
						assertThat(browser.pageSource()).contains("original_product");
				
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/profile";
	 * while logged in as an admin;
	 *//*
	@Test
	public void testProfileAdmin() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						// LogIn
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						
						// Going to the profile page; (checking for data of the admin user, some Strings);
						browser.goTo("http://localhost:3333/profile");
						assertThat(browser.pageSource()).contains("admin");
						assertThat(browser.pageSource()).contains("admin@admin.ba");
						assertThat(browser.pageSource()).contains("Izmijeni");
						assertThat(browser.pageSource()).contains("Da");
								
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/profile";
	 * while not logged in at all;
	 * (we redirect the guest of the webpage to the index.html);
	 *//*
	@Test
	public void testProfileNoLogin() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						// No LogIn
												
						// Going to the profile page; (checking for data of the admin user, some Strings);
						// Which we know, that we will direct them to the index page;
						browser.goTo("http://localhost:3333/profile");
						assertThat(browser.pageSource()).doesNotContain("admin");
						assertThat(browser.pageSource()).doesNotContain("admin@admin.ba");
						assertThat(browser.pageSource()).doesNotContain("Izmijeni");
						assertThat(browser.pageSource()).doesNotContain("Da");
						
					}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/korisnik/id";
	 * while logged in as an admin;
	 *//*
	@Test
	public void testSingleUserAdmin() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						// LogIn as Admin;
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
						//Creating a product
						//takes the new attributes that are entered in the "form" and saves;
						MainCategory mc = MainCategory.findMainCategoryByName("Kompjuteri");
						Product.create("original_product", "original_product_description", 10.00, u, mc, "sarajevo");
						Product p = Product.find.byId(1);
						assertNotNull(p);
						// Going to the korisnik page!; 
						// with the id number 2; which is our User "necko";
						//(checking for data of the user, some Strings and product data;)
						browser.goTo("http://localhost:3333/korisnik/2");
						assertThat(browser.pageSource()).contains("necko");
						assertThat(browser.pageSource()).contains("necko@test.com");
						assertThat(browser.pageSource()).contains("Ne");
						assertThat(browser.pageSource()).contains("original_product");
				
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/korisnik/id";
	 * while not logged in at all;
	 *//*
	@Test
	public void testSingleUserNoLogin() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						//No LogIn ;
						
						//Creating a product
						//takes the new attributes that are entered in the "form" and saves;
						MainCategory mc = MainCategory.findMainCategoryByName("Kompjuteri");
						Product.create("original_product", "original_product_description", 10.00, u, mc, "sarajevo");
						Product p = Product.find.byId(1);
						assertNotNull(p);
						// Going to the korisnik page!; 
						// with the id number 2; which is our User "necko";
						//(checking for data of the user, some Strings and product data;)
						// we know we will automatically redirect the guest to the index.html page;
						browser.goTo("http://localhost:3333/korisnik/2");
						assertThat(browser.pageSource()).doesNotContain("necko");
						assertThat(browser.pageSource()).doesNotContain("necko@test.com");
						//at the index.html page we wil find the products name;
						assertThat(browser.pageSource()).contains("original_product");
				
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/editUser/id";
	 * while logged in as the user "necko";
	 * checking for data of the user in the forms;
	 *//*
	@Test
	public void testEditUser() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						// LogIn as user;
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
			
						// Going to the edituser/id.html page!; 
						// with the id number 2; which is our User "necko";
						//(checking for data of the user, some Strings and product data;)
						browser.goTo("http://localhost:3333/editUser/2");
						assertThat(browser.pageSource()).contains("necko");
						assertThat(browser.pageSource()).contains("necko@test.com");
			
				
						}
				});
	}*/
	
	
	/**
	 * Test for : Going on to the webpage "/editUser/id";
	 * while logged in as the user "necko";
	 * checking for data of the user in the forms;
	 * and checking for the changed attributes(username) have been applied for the user;
	 *//*
	@Test
	public void testEditUser() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						// LogIn as user;
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
			
						// Going to the edituser/id.html page!; 
						// with the id number 2; which is our User "necko";
						//(checking for data of the user, some Strings and product data;)
						browser.goTo("http://localhost:3333/editUser/2");
						assertThat(browser.pageSource()).contains("necko");
						assertThat(browser.pageSource()).contains("necko@test.com");
			
						browser.fill("#username").with("neckonecko");
						browser.submit("#nameForm");
						
					//	browser.goTo("http://localhost:3333/korisnik/2");
						assertThat(browser.pageSource()).contains("neckonecko");
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/korisnici";
	 * while logged in as the admin;
	 * and checking for the usernames of users registered;
	 *//*
	@Test
	public void testAllUsers() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						//Register one more user;
						User.createSaveUser("necko2", "password","necko2@test.com");
						User u2 = User.find(3);
						u2.verified = true;
						u2.save();
						// LogIn as admin;
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("admin");
						browser.fill("#password").with("admin");
						browser.submit("#nameForm");
			
						// Going to the korisnici.html page!; 
						//(checking for usernames of the registered users);
						browser.goTo("http://localhost:3333/korisnici");
						assertThat(browser.pageSource()).contains("necko");
						assertThat(browser.pageSource()).contains("necko2");
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/korisnici";
	 * while logged in as a user;
	 * and checking for the usernames of users registered;
	 * whilst knowing that the user will be redirected to the index.html page;
	 *//*
	@Test
	public void testAllUsers() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						//Register one more user;
						User.createSaveUser("necko2", "password","necko2@test.com");
						User u2 = User.find(3);
						u2.verified = true;
						u2.save();
						// LogIn as necko;
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
			
						// Going to the korisnici.html page!; 
						//(checking for usernames of the registered users);
						// knowing we will be redirected to the index.html page;
						browser.goTo("http://localhost:3333/korisnici");
						assertThat(browser.pageSource()).doesNotContain("admin");
						assertThat(browser.pageSource()).doesNotContain("necko2");
						//at the index.html page we will find his username as the current logged in user;
						assertThat(browser.pageSource()).contains("necko");
						}
				});
	}*/
	
	/**
	 * Test for : Going on to the webpage "/korisnici";
	 * while not logged in at all;
	 * and checking for the usernames of users registered;
	 * whilst knowing that the guest will be redirected to the index.html page;
	 *//*
	@Test
	public void testAllUsers() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						//Register a user;
						User.createSaveUser("necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						//Register one more user;
						User.createSaveUser("necko2", "password","necko2@test.com");
						User u2 = User.find(3);
						u2.verified = true;
						u2.save();
						// No LogIn;

						// Trying to go to the korisnici.html page!; 
						//(checking for usernames of the registered users);
						// knowing we will be redirected to the index.html page;
						browser.goTo("http://localhost:3333/korisnici");
						assertThat(browser.pageSource()).doesNotContain("admin");
						assertThat(browser.pageSource()).doesNotContain("necko");
						assertThat(browser.pageSource()).doesNotContain("necko2");
												
						}
				});
	}*/
	
}
	
	

