package helpers;

import java.util.List;

import play.libs.Json;
import models.BPCredit;
import models.Blogger;
import models.FAQ;
import models.MainCategory;
import models.PrivateMessage;
import models.Product;
import models.SubCategory;
import models.User;
import models.Comment;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class JsonHelper {
	
	
	/**
	 * Used
	 * @param u
	 * @return
	 */
	public static ObjectNode jsonUser(User u){
		ObjectNode user = Json.newObject();
		user.put("id", u.id);
		user.put("username", u.username);
		user.put("password", u.password);
		user.put("email", u.email);
		user.put("createdDate", u.createdDate);
		user.put("imagePath", u.imagePath);
		return user;
	}
	public static ObjectNode jsonBlogger(Blogger b){
		ObjectNode blogger = Json.newObject();
		blogger.put("id", b.id);
		blogger.put("name", b.name);
		blogger.put("description", b.description);
		blogger.put("imagePath", b.blogImagePath);
		return blogger;
	}
	
	/**
	 * Used
	 * @param p
	 * @return
	 */
	public static ObjectNode jsonProduct(Product p){
		ObjectNode product = Json.newObject();
		product.put("id", p.id);
		product.put("name", p.name);
		product.put("description", p.description);
		product.put("price", p.price);
		product.put("isSold", p.isSold);
		product.put("isSpecial", p.isSpecial);
		product.put("longDescription", p.longDescription);
		product.put("publishedDate", p.publishedDate);
		product.put("location", p.location);
		product.put("productImagePath", p.productImagePath);
		product.put("ownerUsername", p.owner.username);
		product.put("ownerId", p.owner.id);
		return product;
	}
	

	/**
	 * Used this simple one for now!.
	 *
	 * @param c the c
	 * @return the object node
	 */
	public static ObjectNode jsonSimpleCategory(MainCategory c) {
		ObjectNode category = Json.newObject();
		category.put("id", c.id);
		category.put("name", c.name);
		return category;
	}

	
	public static ObjectNode jsonCategory(MainCategory c){
		ObjectNode category = Json.newObject();
		category.put("id", c.id);
		category.put("name", c.name);
		ArrayNode products = jsonProductList(c.products);
		category.put("product", products);
		ArrayNode subcategories = jsonSubcategoryList(c.subCategories);
		category.put("subCategories", subcategories);
		return category;
	}
	
	public static ObjectNode jsonSubcategory(SubCategory s){
		ObjectNode subcategory = Json.newObject();
		subcategory.put("id", s.id);
		subcategory.put("name", s.name);
		ArrayNode products = jsonProductList(s.products);
		subcategory.put("products", products);
		return subcategory;
	}
	
	public static ObjectNode jsonFAQ(FAQ f){
		ObjectNode faq = Json.newObject();
		faq.put("id", f.id);
		faq.put("question", f.question);
		faq.put("answer", f.answer);
		return faq;
	}
	
	public static ObjectNode jsonMessage(PrivateMessage m){
		ObjectNode message = Json.newObject();
		message.put("id", m.id);
		message.put("content", m.content);
		ObjectNode sender = jsonUser(m.sender);
		message.put("sender", sender);
		ObjectNode receiver = jsonUser(m.receiver);
		message.put("receiver", receiver);
		return message;
	}
	
	public static ObjectNode jsonComment(Comment c){
		ObjectNode comment = Json.newObject();
		comment.put("id", c.id);
		comment.put("content", c.content);
		ObjectNode author = jsonUser(c.author);
		comment.put("author", author);
		return comment;
	}
	
	public static ObjectNode jsonCredit(BPCredit c){
		ObjectNode credit = Json.newObject();
		credit.put("id", c.id);
		credit.put("credit", c.credit);
		ObjectNode user = jsonUser(c.creditOwner);
		credit.put("creditOwner", user);
		return credit;
	}
	
	
	

	
	public static ArrayNode jsonUserList(List<User> users){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(User u: users){
			ObjectNode user = jsonUser(u);
			arrayNode.add(user);
		}
		return arrayNode;
	}
	
	public static ArrayNode jsonSubcategoryList(List<SubCategory> subcategories){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(SubCategory s: subcategories){
			ObjectNode subcategory = jsonSubcategory(s);
			arrayNode.add(subcategory);
		}
		return arrayNode;
	}
	
	public static ArrayNode jsonCategoryList(List<MainCategory> categories){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(MainCategory c: categories){
			ObjectNode category = jsonCategory(c);
			arrayNode.add(category);
		}
		return arrayNode;
	}
	
	public static ArrayNode jsonProductList(List<Product> products){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(Product p: products){
			ObjectNode product = jsonProduct(p);
			arrayNode.add(product);
		}
		return arrayNode;
	}
	
	public static ArrayNode jsonMessageList(List<PrivateMessage> messages){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(PrivateMessage m: messages){
			ObjectNode message = jsonMessage(m);
			arrayNode.add(message);
		}
		return arrayNode;
	}

	public static ArrayNode jsonCommentList(List<Comment> comments){
		ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
		for(Comment c: comments){
			ObjectNode comment = jsonComment(c);
			arrayNode.add(comment);
		}
		return arrayNode;
	}
	
}
