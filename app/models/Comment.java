package models;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

public class Comment extends Model{

	@Id
	public int id;
	
	@Required
	public String content;

	@CreatedTimestamp
	public Date createdAt;

	@UpdatedTimestamp
	public Date updatedAt;
	
	@ManyToOne
	public User author;

	static Finder<Integer, Comment> find = new Finder<Integer, Comment>(Integer.class, Comment.class);
	
	public Comment(String content, Date createdAt, User author) {
		this.content = content;
		this.createdAt = createdAt;
		this.author = author;
	}

	public Comment() {
		this.content = "No content";
		this.createdAt = null;
		this.author = null;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	public static Comment create(String content, Date createdAt, User author)
	{
		Comment newComment = new Comment(content, createdAt, author);
		newComment.save();
		return newComment;
	}
	
	public static Comment find(int id)
	{
		return find.byId(id);
	}
	
	public static void delete(int id)
	{
		Comment.find.byId(id).delete();
	}

}
