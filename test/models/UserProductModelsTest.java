package models;

import org.junit.*;

import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class UserProductModelsTest extends WithApplication {
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}
	/*
	@Test
	public void testCreateUser() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		assertEquals(u.username, "neko");
		assertEquals(u.email, "neko@gmail.com");

	}
	
	@Test
	public void testFindNonExistingUser() {
		User u = User.find(1000);		
		assertNull(u);
	}
	
	@Test
	public void testDeleteUser() {
		User u = User.createSaveUser("neko", "12345","neko@gmail.com");
		assertNotNull(u);
		User.delete(4);
		User b = User.find(4);
		assertNull(b);
	}
	
	@Test
	public void testCreateProduct() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		MainCategory mc = MainCategory.findMainCategoryByName("Kompjuteri");
		SubCategory sc = SubCategory.findSubCategoryByName("Laptopi");
		Product.create("product1", "product1 description", 100.00, u, mc, sc, "sarajevo");
		Product p = Product.find.byId(1);
		assertNotNull(p);
		assertEquals(p.name, "product1");
		assertEquals(p.description, "product1 description");
		assertEquals(p.price, 100,00);
		assertEquals(p.mainCategory.name, "Kompjuteri");
		assertEquals(p.availability, "sarajevo");
	}
	
	@Test
	public void testFindNonExistingProduct() {
		Product p = Product.find.byId(100);
		assertNull(p);
	}
	
	@Test
	public void testDeleteProduct() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(2);
		MainCategory mc = MainCategory.findMainCategoryByName("Kompjuteri");
		SubCategory sc = SubCategory.findSubCategoryByName("Laptopi");
		Product.create("product1", "product1 description", 100.00, u, mc, sc, "sarajevo");
		Product.delete(1);
		Product p = Product.find.byId(1);
		assertNull(p);
	}
		
	@Test
	public void testIsNotAdmin() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		assertEquals(u.username, "neko");
		assertEquals(u.isAdmin, false);

	}
	
	@Test
	public void testIsAdmin() {
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		assertEquals(u.username, "neko");
		assertEquals(u.isAdmin, false);
		u.setAdmin(true);
		assertEquals(u.isAdmin, true);
		assertEquals(u.email, "neko@gmail.com");
	}
		
	@Test	
	public void testEditUser(){
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		u.setUsername("JohnDoe");
		u.setEmail("johndoe@example.com");
		assertEquals(u.username, "JohnDoe");
		assertEquals(u.email, "johndoe@example.com");
	}
	
	@Test
	public void testMailVerification(){
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		assertEquals(u.verified, false);
	}
	
	@Test
	public void testChangePassword(){
		User.createSaveUser("neko", "12345","neko@gmail.com");
		User u = User.find(4);
		assertNotNull(u);
		u.setPassword("password");
		assertEquals(u.password, "password");
	}
	*/
}
