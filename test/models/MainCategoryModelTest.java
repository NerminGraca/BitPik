package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class MainCategoryModelTest extends WithApplication {
	
	@Before
	public void SetUp() {
		fakeApplication(inMemoryDatabase());
	}
	/*
	@Test
	public void testCreateMainCategory() {
		MainCategory.createMainCategory("Vozila");
		MainCategory mc = MainCategory.findMainCategory(1);
		assertNotNull(mc);
		assertEquals(mc.name, "Vozila");
	}
	
	@Test
	public void testFindNonExistingMainCategory() {
		MainCategory mc = MainCategory.findMainCategory(1000);		
		assertNull(mc);
	}
	
	@Test
	public void testDeleteMainCategory() {
		MainCategory.delete(1);
		MainCategory mc = MainCategory.findMainCategory(1);
		assertNull(mc);
	}
	*/
}
