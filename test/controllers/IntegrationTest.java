package controllers;
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

public class IntegrationTest {
	/*
	@Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
   
	
    @Test
    public void renderTemplate() {

    	List<Product> pl = new ArrayList<Product>();
    	List<MainCategory> mcl = new ArrayList<MainCategory>();
        Content html = views.html.index.render(pl, mcl);
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("BitPik");
    }
    */
	
}
