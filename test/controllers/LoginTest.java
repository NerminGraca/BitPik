package controllers;

import org.junit.*;
import com.google.common.collect.ImmutableMap;
import play.mvc.Result;
import play.test.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import models.*;

public class LoginTest extends WithApplication {
	@Before
	public void setup() {
		fakeApplication(inMemoryDatabase(), fakeGlobal());
//		User.createAdmin("admin", "admin", "admin@admin.ba", true);
	}
/*
	@Test
	public void authenticateSuccess() {
		Result result = callAction(
				controllers.routes.ref.UserController.login(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("username", "admin", "password",
								"admin")));
		assertEquals(200, status(result));
	}

	@Test
	public void authenticateFail() {
		Result result = callAction(
				controllers.routes.ref.UserController.login(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("username", "admin", "password",
								"admin")));
		assertEquals(303, status(result));
		//assertEquals(null, session(result).get("username"));
	  //assertFalse(session(result).containsKey("username"));
	}*/
}