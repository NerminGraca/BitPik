package helpers;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import org.junit.*;

import controllers.Application.Contact;
import play.test.WithApplication;

public class ContactTest extends WithApplication {

	// creates new database in memory
	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}

//	@Test
//	public void validateContact() {
//		Contact testContact = new Contact("test@gmail.com", "message");
//		assertNotNull(testContact.email);
//		assertNotNull(testContact.message);
//		assertEquals(testContact.email.contains("@"), true);
//	}

}
