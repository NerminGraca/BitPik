package controllers;

import java.util.List;

import helpers.JsonHelper;
import helpers.SessionHelper;
import models.MainCategory;
import models.Product;
import models.SubCategory;
import models.User;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class JsonController extends Controller{

	/**
	 * Index page for Android;
	 * @param productlist
	 * @return
	 */
	public static Result indexAndroid(List<Product> productList) {
		ArrayNode arr = JsonHelper.jsonProductList(productList);
		return ok(arr);
	}
	
	
	public static Result registration(){
		
		JsonNode json = request().body().asJson();
		String username = json.findPath("username").textValue();
		String password = json.findPath("password").textValue();
		String confirmPassword = json.findPath("confirmPassword").textValue();
		String email = json.findPath("email").textValue();
		if(username.equals(null) || username.isEmpty()){
			Logger.info("Login error, username not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Username not valid."));
		}
		if(password.equals(null) || password.isEmpty()){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Password not valid."));
		}
		if(confirmPassword.equals(null) || confirmPassword.isEmpty() || !password.equals(confirmPassword)){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Password cofirmation error."));
		}
		if(email.equals(null) || email.isEmpty()){
			Logger.info("Login error, email not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Email not valid."));
		}
		User u = User.createSaveUser(username, confirmPassword, email);
		u.save();
		return ok();
	}
	
	public static Result login(){
				
		JsonNode json = request().body().asJson();
		String username = json.findPath("username").textValue();
		String password = json.findPath("password").textValue();
		
		
		if(username.isEmpty()){
			Logger.info("Login error, username not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Username not valid."));
		}
		if(password.isEmpty()){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Password not valid."));
		}
		//new*****
		User u = User.finder(username);
		session().clear();
		session(UserController.SESSION_USERNAME, username);
		return ok(JsonHelper.jsonUser(u));
	}
	
	public static Result changePassword(int id){
		JsonNode json = request().body().asJson();
		String password = json.findPath("password").textValue();
		String confirmPassword = json.findPath("confirmPassword").textValue();
		if(password.equals(null) || password.isEmpty()){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Password not valid."));
		}
		if(confirmPassword.equals(null) || confirmPassword.isEmpty() || !password.equals(confirmPassword)){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Password not valid."));
		}
		User u = User.find(id);
		u.setPassword(confirmPassword);
		return ok(JsonHelper.jsonUser(u));
	}
	
	public static Result sendMessage(int id){
		User sender = SessionHelper.getCurrentUser(ctx());
		JsonNode json = request().body().asJson();
		String content = json.findPath("content").textValue();
		User receiver = User.find(id);
		if(content.equals(null) || content.isEmpty()){
			Logger.info("Login error, content not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Content not valid."));
		}
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
		array.add(JsonHelper.jsonUser(sender));
		array.add(JsonHelper.jsonUser(receiver));
		return ok(array);
	}
	
	public static Result addProduct(){
		User u = SessionHelper.getCurrentUser(ctx());
		JsonNode json = request().body().asJson();
		String name = json.findPath("name").textValue();
		String desc = json.findPath("desc").textValue();
		String longDesc = json.findPath("longDesc").textValue();
		String priceStr = json.findPath("price").textValue();
		double price = Double.parseDouble(priceStr);
		MainCategory mainCategory = MainCategory.findMainCategoryByName(json.findPath("mainCategory").textValue());
		SubCategory subCategory = SubCategory.findSubCategoryByName(json.findPath("subCategory").textValue());
		String availability = json.findPath("availability").textValue();
		if(name.equals(null) || name.isEmpty()){
			Logger.info("Login error, username not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Name not valid."));
		}
		if(desc.equals(null) || desc.isEmpty()){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Description not valid."));
		}
		if(longDesc.equals(null) || longDesc.isEmpty()){
			Logger.info("Login error, description not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Description cofirmation error."));
		}
		if(mainCategory.equals(null)){
			Logger.info("Login error, category not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Category not valid."));
		}
		if(subCategory.equals(null)){
			Logger.info("Login error, subcategory not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Subcategory not valid."));
		}
		if(availability.equals(null)){
			Logger.info("Login error, availability not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Availability not valid."));
		}
		Product p = Product.create(name, desc, longDesc, price, u, mainCategory, subCategory, availability);
		p.save();
		return ok();
	}
	
	public static Result editProduct(int id){
		User u = SessionHelper.getCurrentUser(ctx());
		JsonNode json = request().body().asJson();
		String name = json.findPath("name").textValue();
		String desc = json.findPath("desc").textValue();
		String longDesc = json.findPath("longDesc").textValue();
		String priceStr = json.findPath("price").textValue();
		double price = Double.parseDouble(priceStr);
		MainCategory mainCategory = new MainCategory(json.findPath("mainCategory").textValue());
		SubCategory subCategory = new SubCategory(json.findPath("subCategory").textValue(), mainCategory);
		String availability = json.findPath("availability").textValue();
		if(name.equals(null) || name.isEmpty()){
			Logger.info("Login error, username not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Name not valid."));
		}
		if(desc.equals(null) || desc.isEmpty()){
			Logger.info("Login error, password not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Description not valid."));
		}
		if(longDesc.equals(null) || longDesc.isEmpty()){
			Logger.info("Login error, description not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Description cofirmation error."));
		}
		if(mainCategory.equals(null)){
			Logger.info("Login error, category not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Category not valid."));
		}
		if(subCategory.equals(null)){
			Logger.info("Login error, subcategory not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Subcategory not valid."));
		}
		if(availability.equals(null)){
			Logger.info("Login error, availability not valid");
			ObjectNode message = Json.newObject();
			return badRequest(message.put("error", "Availability not valid."));
		}
		Product p = Product.find.byId(id);
		p.setName(name);
		p.setDesc(longDesc);
		p.setCategory(mainCategory);
		p.setAvailability(availability);
		p.setPrice(price);
		p.setSubCategory(subCategory);
		p.save();
		return ok();
	}
	
}
