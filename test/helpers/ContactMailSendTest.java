/*package helpers;
import java.util.ArrayList;
import java.util.List;

import models.MainCategory;
import models.Product;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.twirl.api.Content;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.apache.commons.mail.EmailException;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

public class ContactMailSendTest {

	private MailHelper mailHelper;

	@Before
	public void setUp() {
	 mailHelper = new MailHelper();
	 //clear Mock JavaMail box
	 Mailbox.clearAll();
	}

	@Test
	public void testSendInRegualarJavaMail() throws MessagingException, IOException, EmailException {

	 String subject = "Test1";
	 String body = "Test Message1";
	 mailHelper.sendContactMessage("test.dest@nutpan.com", "Hello world");
	  Session session = Session.getDefaultInstance(new Properties());
	 Store store = session.getStore("pop3");
	 store.connect("nutpan.com", "test.dest", "password");

	 Folder folder = store.getFolder("inbox");

	 folder.open(Folder.READ_ONLY);
	 Message[] msg = folder.getMessages();

	 assertTrue(msg.length == 1);
	 assertEquals(subject, msg[0].getSubject());
	 assertEquals(body, msg[0].getContent());
	 folder.close(true);
	 store.close();
	}
	}
*/