package controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.MainCategory;
import models.Product;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class ApplicationTest {
	/*
   
   @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
   
    @Test
    public void renderTemplate() {
    	String s = "a";
    	List<Product> pl = new ArrayList<Product>();
    	List<String> sl = new ArrayList<String>();
    	List<MainCategory> mcl = new ArrayList<MainCategory>();
        Content html = views.html.index.render(s, pl, sl, mcl);
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("BitPik");
    }
    
	*/
}
