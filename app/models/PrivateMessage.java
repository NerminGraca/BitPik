package models;


import javax.persistence.Id;
import javax.persistence.ManyToOne;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebeaninternal.server.core.Message;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity

public class PrivateMessage extends Model{
	
	@Id
	public int id;
	
	@Required
	public String content;
	
	@ManyToOne
	public User sender;

	@ManyToOne
	public User receiver;
	
	public static Finder<Integer, PrivateMessage> find = new Finder<Integer, PrivateMessage>(Integer.class, PrivateMessage.class);

	

	public PrivateMessage(String content, User sender, User receiver) {
		this.content = content;
		this.sender = sender;
		this.receiver = receiver;
	}

	public PrivateMessage() {

		this.content = "no content";
		this.sender = null;
		this.receiver = null;

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	
	public static PrivateMessage create(String content, User sender, User receiver) {
		PrivateMessage newMessage = new PrivateMessage(content, sender, receiver);
		newMessage.save();
		return newMessage;
	}
	
	public static void delete(int id) {
		  find.ref(id).delete();
	}

	

}


