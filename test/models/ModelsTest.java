package models;

import java.util.List;

import models.*;

import org.junit.*;

import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}
	/*
	@Test
	public void testCreate() {
		User.create("neko", "12345");
		User u = User.find(1);
		
		assertNotNull(u);
		assertEquals(u.username, "neko");
		assertEquals(u.password, "12345");
	}
	
	@Test
	public void testFindNonExisting() {
		User u = User.find(1000);
		
		assertNull(u);
	}
	
	@Test
	public void testDelete() {
		User.create("neko", "12345");
		User.delete(1);
		User b = User.find(1);
		assertNull(b);
	}
	*/
}
