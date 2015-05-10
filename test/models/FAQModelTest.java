package models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import models.FAQ;

import org.junit.Before;
import org.junit.Test;

import play.test.WithApplication;
import static org.junit.Assert.*;

public class FAQModelTest extends WithApplication {
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}

	/**
	 * Test creates one FAQ, checks if FAQ with that id exists, and checks if
	 * question and answer are correct.
	 */
//	@Test
//	public void testCreateFaq() {
//		FAQ.create("pitanje", "odgovor");
//		FAQ f = FAQ.finder(6);
//		assertNotNull(f);
//		assertEquals(f.question, "pitanje");
//		assertEquals(f.answer, "odgovor");
//	}
//	
//	@Test
//	public void testFindNonExistingFaq() {
//		FAQ f = FAQ.finder(1000);
//		
//		assertNull(f);
//	}
//	
//	/**
//	 * Test creates one FAQ, checks if FAQ with that id exists, and puts values
//	 * using setters. After that, test checks if values are correct.
//	 */
//	@Test
//	public void testEditFaq() {
//		FAQ.create("pitanje", "odgovor");
//		FAQ f = FAQ.finder(6);
//		assertNotNull(f);
//		f.setQuestion("Pitanje2");
//		f.setAnswer("Odgovor2");
//		assertEquals(f.question, "Pitanje2");
//		assertEquals(f.answer, "Odgovor2");
//	}
//
//	/**
//	 * Test creates one FAQ, checks if FAQ with that id exists, and deletes FAQ.
//	 * After that, test checks if deleted FAQ is null.
//	 */
//	@Test
//	public void testDeleteFaq() {
//		FAQ.create("pitanje", "odgovor");
//		FAQ f = FAQ.finder(6);
//		assertNotNull(f);
//		FAQ.delete(6);
//		f = FAQ.finder(6);
//		assertNull(f);		
//	}
}
