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

public class ProductTest extends WithApplication {
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}


	
	/**
	 * Creating two products and checking whether in database
	 * the attributes of the second product;
	 */
	@Test
	public void testcheckProduct2() {
		User.createSaveUser("neko2", "12345","neko2@gmail.com");
		User u = User.find(2);
		String hashedPass=HashHelper.createPassword("12345");
		assertNotNull(u);
		assertEquals(u.username, "neko2");
	//	assertEquals(u.password, hashedPass);
		assertEquals(u.email, "neko2@gmail.com");
		
		Product.create("original_product2", "original_product_description", 10.00, "vozila", "sarajevo", u);
		Product p = Product.find.byId(1);
		assertNotNull(p);
		assertEquals(p.name, "original_product2");
		assertEquals(p.desc, "original_product_description");
		assertEquals(p.category, "vozila");
		assertEquals(p.availability, "sarajevo");
	}
	
	
	
	
	/**
	 * UPDATE TEST:
	 * Test for editing/changing the name and the description of the product;
	 * 1. We add a user;
	 * 2. We publish a product;
	 * 3. We find the product;
	 * 4. We edit some of the attributes of the found product;
	 * 5. We find the product again;
	 * 6. We check whether the new given name and descriptions are found in the product; 
	 */
	@Test
	public void testCreateEditedProduct() {
		
							User.createSaveUser("Necko", "password","necko@test.com");
							User u = User.find(2);
							//takes the new attributes that are entered in the "form" and saves;
							Product.create("original_product", "original_product_description", 10.00, "kompjuteri", "sarajevo", u);
							Product p = Product.find.byId(1);
							assertNotNull(p);
							assertEquals(p.name, "original_product");
							assertEquals(p.desc, "original_product_description");
							assertEquals(p.category, "kompjuteri");
							assertEquals(p.availability, "sarajevo");
														
							Product findProducd = Product.find.byId(1);
							findProducd.setName("new_product");
							findProducd.setDesc("new_product_description");
							findProducd.save();
							
							Product foundProduct = Product.find.byId(1);
							// checks whether the first product added is not null
							assertNotNull(foundProduct);
							// checks whether the first products name is the new name that we have set;
							assertEquals(foundProduct.name, "new_product");
							// checks whether the first products desc is the new desc that we have set;
							assertEquals(foundProduct.desc, "new_product_description");
					}
		
	/**
	 * Creates a user;
	 * logs in;
	 * Publishes a product;
	 * goes to editProduct.html;
	 * edits name, desc, price, category and availability;
	 * redirects to showProduct/id
	 * checks that the new name is one the showProduct/id.html page;
	 * checks that the new product has changed the name and desc;
	 */
	@Test
	public void testSaveEditProduct() {
			running(testServer(3333, fakeApplication(inMemoryDatabase())),
					HTMLUNIT, new Callback<TestBrowser>() {
						public void invoke(TestBrowser browser) {
							User.createSaveUser("Necko", "password","necko@test.com");
							User u = User.find(2);
							u.verified = true;
							u.save();
							browser.goTo("http://localhost:3333/login");
							browser.fill("#username").with("Necko");
							browser.fill("#password").with("password");
							browser.submit("#nameForm");
														
							assertNotNull(u);
							
							//takes the new attributes that are entered in the "form" and saves;
							Product.create("original_product", "original_product_description", 10, "kompjuteri", "sarajevo", u);
							Product findProduct = Product.find.byId(1);
													
							assertNotNull(findProduct);
							
							browser.goTo("http://localhost:3333/editProduct/1");
							browser.fill("#name").with("newP");
							browser.fill("#desc").with("newD");
							browser.fill("#price").with("1");
							browser.fill("#category").with("Vozila");
							browser.fill("#availability").with("Livanjski");
							browser.submit("#productForm");
							
							assertThat(browser.pageSource()).contains("newP");
							assertThat(browser.pageSource()).contains("newD");
						
						}
					});
	}
	
	
	/**
	 * Test whether the 3-rd non existing product is Null;
	 */
	@Test
	public void testFindNonExistingProduct() {
		Product p = Product.find.byId(3);
		assertNull(p);
	}
	
	
	
	/**
	 * Test for deleting 2 products;
	 */
	@Test
	public void testDelete2Products() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User.createSaveUser("Necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("Necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
							
						Product.create("original_product", "original_product_description", 10, "kompjuteri", "sarajevo", u);
						Product.create("original_product2", "original_product_description2", 20, "kompjuteri", "sarajevo", u);
						Product findProduct = Product.find.byId(1);
						assertNotNull(findProduct);
						
						Product.delete(2);
						Product p=Product.find.byId(2);
						assertNull(p);
						
						Product.delete(1);
						Product k =Product.find.byId(1);
						assertNull(k);
						
						}
				});
			}
	
	/**
	 * Creating one product and checking whether in database
	 * the attributes of the only product;
	 */
	@Test
	public void testcheckProduct() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(2);
		String hashedPass=HashHelper.createPassword("12345");
		assertNotNull(u);
		assertEquals(u.username, "neko");
	//	assertEquals(u.password, hashedPass);
		assertEquals(u.email, "neko@gmail.com");
		
		Product.create("original_product", "original_product_description", 10.00, "kompjuteri", "sarajevo", u);
		Product p = Product.find.byId(1);
		assertNotNull(p);
		assertEquals(p.name, "original_product");
		assertEquals(p.desc, "original_product_description");
		assertEquals(p.category, "kompjuteri");
		assertEquals(p.availability, "sarajevo");
	}

		
	
	
	/**
	 * Creating one product and checking if the showProduct.html contains the information
	 * of the product added/created;
	 */
	@Test
	public void testShowProduct() {
			running(testServer(3333, fakeApplication(inMemoryDatabase())),
					HTMLUNIT, new Callback<TestBrowser>() {
						public void invoke(TestBrowser browser) {
							User.createSaveUser("Necko", "password","necko@test.com");
							User u = User.find(2);
							u.verified = true;
							u.save();
							//takes the new attributes that are entered in the "form" and saves;
							Product.create("original_product", "original_product_description", 10.00, "kompjuteri", "sarajevo", u);
							browser.goTo("http://localhost:3333/showProduct/1");
							assertThat(browser.pageSource()).contains("original_product");
							assertThat(browser.pageSource()).contains("original_product_description");
							assertThat(browser.pageSource()).contains("sarajevo");
							assertThat(browser.pageSource()).contains("10.0");
							assertThat(browser.pageSource()).contains("kompjuteri");

						}
					});
	}
	
	/**
	 * Creating one product and checking if the editProduct.html contains the information
	 * of the product edited and its attributes;
	 */
	@Test
	public void testEditProduct() {
			running(testServer(3333, fakeApplication(inMemoryDatabase())),
					HTMLUNIT, new Callback<TestBrowser>() {
						public void invoke(TestBrowser browser) {
							User.createSaveUser("Necko", "password","necko@test.com");
							User u = User.find(2);
							u.verified = true;
							u.save();
							//takes the new attributes that are entered in the "form" and saves;
							Product.create("original_product", "original_product_description", 10.00, "kompjuteri", "sarajevo", u);
							browser.goTo("http://localhost:3333/editProduct/1");
							assertThat(browser.pageSource()).contains("original_product");
							assertThat(browser.pageSource()).contains("10.0");
							
						}
					});
	}
	
	
	/**
	 * Test for deleting a product;
	 */
	@Test
	public void testDeleteProduct() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())),
				HTMLUNIT, new Callback<TestBrowser>() {
					public void invoke(TestBrowser browser) {
						User.createSaveUser("Necko", "password","necko@test.com");
						User u = User.find(2);
						u.verified = true;
						u.save();
						browser.goTo("http://localhost:3333/login");
						browser.fill("#username").with("Necko");
						browser.fill("#password").with("password");
						browser.submit("#nameForm");
							
						Product.create("original_product", "original_product_description", 10, "kompjuteri", "sarajevo", u);
						Product findProduct = Product.find.byId(1);
						assertNotNull(u);
						
						Product.delete(1);
						Product p=Product.find.byId(1);
						assertNull(p);
						}
				});
			}
	
	
	}



	

