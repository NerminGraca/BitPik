package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Comment extends Model{

	@Id
	public int id;
	
	@Required
	public String content;

	@CreatedTimestamp
	public Date createdAt;
	
	@ManyToOne
	public User author;

	public static Finder<Integer, Comment> find = new Finder<Integer, Comment>(Integer.class, Comment.class);

	/**
	 * Constructor with three parameters
	 * @param content
	 * @param createdAt
	 * @param author
	 */
	
	public Comment(String content, Date createdAt, User author) {
		this.content = content;
		this.createdAt = createdAt;
		this.author = author;
	}
	
	/**
	 * Default constructor
	 */

	public Comment() {
		this.content = "No content";
		this.createdAt = null;
		this.author = null;
	}

	/**
	 * Getter for content
	 * @return content
	 */
	
	public String getContent() {
		return content;
	}
	
	/**
	 * Setter for content
	 * @param content
	 */
	
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Getter for create date
	 * @return createdAt
	 */
	
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter for create date
	 * @param createdAt
	 */
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for authod
	 * @return author
	 */
	
	public User getAuthor() {
		return author;
	}

	/**
	 * Setter for authod
	 * @param author
	 */
	
	public void setAuthor(User author) {
		this.author = author;
	}
	
	/**
	 * Method creates new comment with content,
	 * create date and author.
	 * @param content
	 * @param createdAt
	 * @param author
	 * @return newComment
	 */
	
	public static Comment create(String content, Date createdAt, User author)
	{
		Comment newComment = new Comment(content, createdAt, author);
		newComment.save();
		return newComment;
	}
	
	/**
	 * Finder for comments (by id)
	 * @param id
	 * @return Comment
	 */
	
	public static Comment find(int id)
	{
		return find.byId(id);
	}
	
	/**
	 * Method deletes comment with given id
	 * @param id
	 */
	
	public static void delete(int id)
	{
		Comment.find.byId(id).delete();
	}

}
