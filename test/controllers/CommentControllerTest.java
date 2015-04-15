package controllers;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import helpers.HashHelper;

import java.io.File;
import java.util.Date;
import java.util.List;

import models.*;

import org.junit.*;
import org.mockito.internal.matchers.Find;

import com.google.common.io.Files;

import play.Logger;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import controllers.UserController;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import models.User;
import play.test.*;
import play.i18n.Messages;
import play.libs.F.*;
import static org.fest.assertions.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.junit.Assert.assertNotNull;

public class CommentControllerTest extends WithApplication{

	@Before
	public void setUp() {
		fakeApplication(inMemoryDatabase());
	}
	
	@Test
	public void testAddComment() {
		Product p = new Product();
		CommentController.addComment(p.id);
		Comment c = Comment.find(1);
		c.save();
		assertNotNull(c);
		assertEquals(c.content, "no content");
	}
	
	@Test
	public void testDeleteComment(){
		Product p = new Product();
		CommentController.addComment(p.id);
		Comment c = Comment.find(1);
		c.save();
		assertNotNull(c);
		CommentController.deleteComment(c.id, p.id);
		assertNull(c);
	}
}

