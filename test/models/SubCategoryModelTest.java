package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;

public class SubCategoryModelTest extends WithApplication {
	
	@Before
	public void SetUp() {
		fakeApplication(inMemoryDatabase());
	}
	/*
	@Test
	public void testCreateSubCategory() {
		MainCategory mc = MainCategory.findMainCategory(1);
		SubCategory.createSubCategory("Podvozila", mc);
		SubCategory sc = SubCategory.findSubCategoryByName("Podvozila");
		assertNotNull(sc);
		assertEquals(sc.name, "Podvozila");
	}
	
	@Test
	public void testFindNonExistingMainCategory() {
		SubCategory mc = SubCategory.findSubCategory(1000);		
		assertNull(mc);
	}
	
	@Test
	public void testEditSubCategory() {
		SubCategory sc = SubCategory.findSubCategory(1);
		assertNotNull(sc);
		assertEquals(sc.name, "Dijelovi i oprema");
		sc.setName("Promjena");
		assertEquals(sc.name, "Promjena");
	}
	
	@Test
	public void testDeleteSubCategory() {
		SubCategory.delete(1);
		SubCategory sc = SubCategory.findSubCategory(1);
		assertNull(sc);
	}
	*/
}
