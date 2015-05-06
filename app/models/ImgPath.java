package models;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import play.Logger;
import play.Play;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ImgPath extends Model{
	
	@Id
	public int id;
	
	public String public_id;

	@Required
	public String image_url;
	
	public String secret_image_url;
	
	@ManyToOne
	public Product product;
	
	public Blogger blogger;
	
	@OneToOne
	public User userImage;
	
	public ImgPath() {
		// TODO Auto-generated constructor stub
	}
	
	public Blogger getBlogger() {
		return blogger;
	}

	public void setBlogger(Blogger blogger) {
		this.blogger = blogger;
	}
	
	public ImgPath(String image_url, Product product) {
		this.image_url = image_url;
		this.product = product;
	}
	public ImgPath(String image_url, Blogger blogger){
		this.image_url = image_url;
		this.blogger = blogger;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImgPath() {
		return image_url;
	}

	public void setImgPath(String imgPath) {
		this.image_url = imgPath;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	// NOVO!!!
	public static Cloudinary cloudinary = new Cloudinary(Play.application().configuration().getString("cloudKey"));
	
	public static Finder<Integer, ImgPath> find = new Finder<Integer, ImgPath>(Integer.class, ImgPath.class);
	
	/**
	 * Creates the default image for the product;
	 * @param p
	 * @return
	 */
	public static ImgPath createDefault(Product p){
		ImgPath i= new ImgPath();
		i.public_id = "no-img_mqapfa";
		i.image_url = "http://res.cloudinary.com/bitpik/image/upload/v1430753378/no-img_mqapfa.jpg";
		i.secret_image_url = "https://res.cloudinary.com/bitpik/image/upload/v1430753378/no-img_mqapfa.jpg";
		i.setProduct(p);
		i.save();
		p.imgPathList.add(i);
		p.save();
		return i;
	}
	
	/**
	 * Creates the image for the product;
	 * @param public_id
	 * @param image_url
	 * @param secret_image_url
	 * @param p
	 * @return
	 */
	public static ImgPath create(String public_id, String image_url, String secret_image_url, Product p){
		ImgPath i = new ImgPath();
		i.public_id = public_id;
		i.image_url = image_url;
		i.secret_image_url = secret_image_url;
		i.setProduct(p);
		i.save();
		p.imgPathList.add(i);
		p.save();
		return i;
	}
	
	/**
	 * Creates the image for the User;
	 * @param public_id
	 * @param image_url
	 * @param secret_image_url
	 * @param u
	 * @return
	 */
	public static ImgPath create(String public_id, String image_url, String secret_image_url, User u){
		ImgPath i = new ImgPath();
		i.public_id = public_id;
		i.image_url = image_url;
		i.secret_image_url = secret_image_url;
		u.setImagePath(i.image_url);
		u.imagePathOne = i;
		i.userImage = u;
		i.save();
		u.save();
		return i;
	}
	
	/**
	 * Uploads the image to our cloudinary and later using the create method
	 * assigns the image to the product;
	 * 
	 * @param image
	 * @param p
	 * @return
	 */
	public static ImgPath create(File image, Product p){
		Map result;
		try {
			// null moze bit sa parametrima, da slika ima tu velicinu, svasta
			// nesto...
			// cloudinary vraca result; Json - tj. MAPU! (id, url, secret_url
			// and more meta data;)
			result = cloudinary.uploader().upload(image, null);
			return create(result, p);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Attaches the picture to the Product;
	 * And gets the picture from cloudinary;
	 * @param uploadResult
	 * @param p
	 * @return
	 */
	public static ImgPath create(Map uploadResult, Product p){
		ImgPath i = new ImgPath();
		
		i.public_id = (String) uploadResult.get("public_id");
		Logger.debug(i.public_id);
		i.image_url = (String) uploadResult.get("url");
		Logger.debug(i.image_url);
		i.secret_image_url = (String) uploadResult.get("secure_url");
		Logger.debug(i.secret_image_url);
		i.setProduct(p);
		i.save();
		p.imgPathList.add(i);
		p.save();
		return i;
	}
	
	/**
	 * Uploads the image to our cloudinary and later using the create method
	 * assigns the image to the User;
 	 * @param image
	 * @param u
	 * 
	 */
	public static ImgPath create(File image, User u){
		Map result;
		try {
			// null moze bit sa parametrima, da slika ima tu velicinu, svasta nesto...
			//cloudinary vraca result; Json - tj. MAPU! (id, url, secret_url and more meta data;)
			result = cloudinary.uploader().upload(image, null);
			return create(result, u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Attaches the picture to the User;
	 * And gets the picture from cloudinary;
	 * @param uploadResult
	 * @param u
	 * @return
	 */
	public static ImgPath create(Map uploadResult, User u){
		ImgPath i = new ImgPath();
		
		i.public_id = (String) uploadResult.get("public_id");
		Logger.debug(i.public_id);
		i.image_url = (String) uploadResult.get("url");
		Logger.debug(i.image_url);
		i.secret_image_url = (String) uploadResult.get("secure_url");
		Logger.debug(i.secret_image_url);
		u.setImagePath(i.image_url);
		u.imagePathOne = i;
		i.userImage = u; 
		i.save();
		u.save();
		return i;
	}

	/**
	 * Gets the picture from cloudinary, and transforms it into the parameters
	 * which are the height and width
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public String getSize(int width, int height){
		String url = cloudinary
				.url()
				.format("jpg")
				.transformation(
						new Transformation().width(width).height(height))
				.generate(public_id);

		return url;
	}



}
