package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class FAQ extends Model {
	
	@Id
	public int id;
	
	@Required
	public String question;
	
	@Required
	public String answer;
	
	
	/**
	 * Default constructor
	 */
	
	public FAQ(){
		this.question = "Pitanje";
		this.answer  = "Odgovor";
	}
	
	/**
	 * Contructor with two parameters
	 * @param question
	 * @param answer
	 */
	
	public FAQ(String question, String answer){
		this.question = question;
		this.answer  = answer;
	}
	
	public static Finder<Integer, FAQ> find = new Finder<Integer, FAQ>(Integer.class, FAQ.class);

	
	/**
	 * Getter for question
	 * @return question
	 */
	
	public String getQuestion()
	{
		return this.question;
	}
	
	/**
	 * Getter for answer
	 * @return answer
	 */
	
	public String getAnswer()
	{
		return this.answer;
	}
	
	/**
	 * Setter for question
	 * @param question
	 */
	
	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	/**
	 * Setter for answer
	 * @param answer
	 */
	
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	
	/**
	 * Method creates new FAQ with question and answer
	 * @param question
	 * @param answer
	 * @return newFaq
	 */
	
	public static FAQ create(String question, String answer)
	{
		FAQ newFaq = new FAQ(question, answer);
		newFaq.save();
		return newFaq;
	}
	
	public static FAQ finder(int id) {
		return find.where().eq("id", id).findUnique();
	}
	
	public static void delete(int id) {
		FAQ.finder(id).delete();
	}
}