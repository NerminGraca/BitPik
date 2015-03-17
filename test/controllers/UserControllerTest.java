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
	 * This current test is for the following : 
	 * 1. Going on to the page "/profile";
	 * 2. While logged in as a User with the username : "necko";
	 * 3. And testing whether the "/profile" page contains the info and data about the
	 * user logged in.
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
	 *This current test is for the following : 
	 *1. Going on to the page "/profile";
	 *2. While logged in as an admin user;
	 *3. And testing whether the "/profile" page contains the info and data about the
	 * user admin logged in.
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
	 *This current test is for the following : 
	 *1. Going on to the page "/profile";
	 *2. While not logged in at all;
	 *3. We know that the guest will be redirected to the index.html page;
	 *4. Checking and asserting that the page we are then on, coes not contain
	 *the information about any user in this test the info about the admin.
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
	 *This current test is for the following : 
	 *1. Going on to the page "/korisnik/id";
	 *2. While logged in as an admin user;
	 *3. A user is created and that user has added/published a product.
	 *4. Checking & asserting the the korisnik/id page contains the information 
	 *(id of the user that has just added/published the product), that the page
	 *contains the information about the user and the product that he has published. 
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
	 *This current test is for the following : 
	 *1. Going on to the page "/korisnik/id";
	 *2. While not logged in at all;
 	 *3. A user is created and that user has added/published a product.
 	 *4. We assert and check that the page the our guest/visiot is on does not 
 	 *contain the information about the user just created and the information
 	 *about the product.
 	 *Since we know that he will be automatically redirected to the index.html page;
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
	 * This current test is for the following : 
	 * 1. Going on to the page "/editUser/id" & later korisnik/id;
	 * 2. While logged in as a User with the username : "necko";
	 * 3. We go to the editUser/id page and we assert and check whether
	 * the page contains information about the user. Actually what we are checking
	 * is that the forms for editing the users name or email etc.. contain the 
	 * current values of the user.
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
	 * This current test is for the following : 
	 * 1. Going on to the page "/editUser/id";
	 * 2. While logged in as a User with the username : "necko";
	 * 3. We go to the editUser/id page and we assert and check whether
	 * the page contains information about the user. Actually what we are checking
	 * is that the forms for editing the users name or email or etc.. contain the 
	 * current values of the user.
	 * 4. We then change the users username to a new String and submit/apply the change.
	 * 5. Then we check and assert that the changed attributes(username) have been applied for the user;
	 *//*
	@Test
	public void testSaveEditUser() {
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
	 *This current test is for the following : 
	 *1. Going on to the page "/korisnici";
	 *2. While logged in as an admin user;
	 *3. We create two users with their attributes, username, password and email.
	 *3. Then we assert and check that the page /korisnici contains the usernames
	 *of the just created two users. As we know that this page should contain all
	 *the usernames of all the users registered. Which is only visible to the 
	 *admin user.
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
	 *This current test is for the following : 
	 *1. Going on to the page "/korisnici";
	 *2. While logged in as an user with the username necko;
	 *3. We create two users with their attributes, username, password and email.
	 *4. Then we assert and check that the page /korisnici does not contain the 
	 *usernames of users registered; As we know we will redirect the user logged
	 *in to the index.html page. As the only one who can visit the /korisnici
	 *page is the admin. (or any other user who's attribute isAdmin is true);
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
	/**
	 *This current test is for the following : 
	 *1. Going on to the page "/korisnici";
	 *2. While not logged in at all;
	 *3. We create two users with their attributes, username, password and email.
	 *4. Then we assert and check that the page /korisnici does not contain the 
	 *usernames of users registered; As we know we will redirect the user logged
	 *in to the index.html page. As the only one who can visit the /korisnici
	 *page is the admin. (or any other user who's attribute isAdmin is true);
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
	
	

