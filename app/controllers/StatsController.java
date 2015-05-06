package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import models.*;
import helpers.SessionHelper;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;
import views.*;
import views.html.*;
import models.Blogger;
import models.Product;
import models.Statistics;
import models.User;

/**
 * This is the Statistics controller;
 * Which controls the function of all of the products and their 
 * statistic information, as in how many times has an article 
 * been clicked on, been commented on, and many more statistics 
 * regarding an article published on the web page;
 * @author Necko
 *
 */
public class StatsController extends Controller {
	
	public static List<Product> allProducts ;
	static final String statsFilePath = "./statistic/bitpik_products/";



	/**
	 * Method showStats() gets us the page statsproducts
	 * where we give the list of all products to the User
	 * who has published all of the products, it does not matter
	 * whether he is still selling them or has already sold them.
	 * As they will all be listed with their statistics of 
	 * each of the item.
	 * 
	 * @return
	 */
	public static Result showStats() {
		User currentUser = SessionHelper.getCurrentUser(ctx());
		if(currentUser != null && currentUser.username.equals("blogger")){
			List<Blogger> bloggerList = Blogger.find.all();
			return ok(blog.render(bloggerList,currentUser));
		}

		// Null Catching
		if (currentUser == null) {
			return redirect(routes.Application.index());
		}
		
		allProducts = ProductController.findProduct.where().eq("owner.username", currentUser.username).findList();
		
		applyTheStringViewForStats(currentUser);
	
		return ok(statsproducts.render(currentUser, allProducts));

	}
	
	/**
	 * Method that downloads the statistic's file as we have on the statsproducts.html page
	 * in an excel file format (.xls extension);
	 * @return stastFile in an ok, 200 response;
	 */
	public static Result downloadStatistics(){
		User currentUser = SessionHelper.getCurrentUser(ctx());
		// Null Catching
		if (currentUser == null) {
			Logger.of("user").warn("Not registered User tried to access the fucntion > download the statistics file");
			return redirect(routes.Application.index());
		}
		// Method in Statscontroller which generates and totally fills the excel sheet with data needed in the file;
		File statsFile = createStatisticsFile(currentUser);
		response().setContentType("application/x-download");  
		response().setHeader("Content-disposition","attachment; filename=statistics.xls");
		return ok(statsFile);
	
	}
	
	/**
	 * This method is used by the method > downloadStatistics();
	 * Method which creates and saves the Excel file;
	 * Fills the excel file with data from models Product and Statistics, as Product name,
	 * number of clicks and more found in this method;
	 * @return
	 */
		public static File createStatisticsFile(User currentUser){
		
		//TODO WHOLE APP TO USE ONE HREF.
		//final String href = "http://localhost:9000/";
		
		try {
			// creates the "excel sheet" with its name;
			HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet =  workbook.createSheet("Statistics"); 
	        String fileName = UUID.randomUUID().toString().replace("-", "") + ".xls";
	        // creates the folder with the file name;                
	        new File(statsFilePath).mkdirs();
	        File statisticFile = new File(statsFilePath +fileName);
	       
	        applyTheStringViewForStats(currentUser);
			  
	        //SETTING STYLE
	        HSSFCellStyle style = workbook.createCellStyle();
	        style.setBorderTop((short) 6); 
	        style.setBorderBottom((short) 1); 
	        style.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);
	        style.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	        style.setFillPattern(HSSFColor.GREY_80_PERCENT.index);
	        
	        HSSFFont font = workbook.createFont();
	        font.setFontName(HSSFFont.FONT_ARIAL);
	        font.setFontHeightInPoints((short) 10);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.BLACK.index);	        
	        style.setFont(font);
	        style.setWrapText(true);
	        
	        //Header cells (the first header row) and added style !
	        HSSFRow rowhead=   sheet.createRow((short)0);
	        
	        HSSFCell bitpikId = rowhead.createCell(0);
	        bitpikId.setCellValue(new HSSFRichTextString("BitPik id"));
	        bitpikId.setCellStyle(style);
	       
	        HSSFCell productName = rowhead.createCell(1);
	        productName.setCellValue(new HSSFRichTextString("Ime proizvoda"));
	        productName.setCellStyle(style);
	        
	        HSSFCell  productPrice = rowhead.createCell(2);
	        productPrice.setCellValue(new HSSFRichTextString("Cijena"));
	        productPrice.setCellStyle(style);
	        
	        HSSFCell publishedDate = rowhead.createCell(3);
	        publishedDate.setCellValue(new HSSFRichTextString("Datum objave"));
	        publishedDate.setCellStyle(style); 
	        
	        HSSFCell isSold = rowhead.createCell(4);
	        isSold.setCellValue(new HSSFRichTextString("Prodat artikal"));
	        isSold.setCellStyle(style); 
	        
	        HSSFCell isSpecial = rowhead.createCell(5);
	        isSpecial.setCellValue(new HSSFRichTextString("Izdvojen"));
	        isSpecial.setCellStyle(style); 
	        
	        HSSFCell noOfClicks = rowhead.createCell(6);
	        noOfClicks.setCellValue(new HSSFRichTextString("Br. pregleda"));
	        noOfClicks.setCellStyle(style); 
	        
	        HSSFCell noOfComments = rowhead.createCell(7);
	        noOfComments.setCellValue(new HSSFRichTextString("Br. komentara"));
	        noOfComments.setCellStyle(style);
	        
	        rowhead.setHeight((short)20);
	        //Creating rows for each product from the list allProducts. 
	        // starting from the row (number 1.) - we generate each cell with its value;
	        int rowIndex = 1;
	      
	        for (Product product: allProducts){
		        	HSSFRow row=   sheet.createRow(rowIndex);
		        	row.createCell(0).setCellValue(product.id);
		            row.createCell(1).setCellValue(product.name);
		            row.createCell(2).setCellValue(product.price);
		            row.createCell(3).setCellValue(product.publishedDate); 	 
		            row.createCell(4).setCellValue(product.statsProducts.isItSold); 	
		            row.createCell(5).setCellValue(product.statsProducts.isItSpecial); 
		          
		            row.createCell(6).setCellValue(product.statsProducts.noOfClicksS); 	
		            Logger.info("noOfclicks Prod." +product.name + " je > " +product.statsProducts.noOfClicksS);
		            row.createCell(7).setCellValue(product.statsProducts.noOfComments); 
		            Logger.info("noOfcomments Prod." +product.name + " je > " +product.statsProducts.noOfClicksS);
		            rowIndex++;
	        }
	 
	        //rowhead.setRowStyle(style);
	        //auto-sizing all of the columns in the sheet generated;
	        for(int i=0; i<8; i++){
	        	 sheet.autoSizeColumn((short) i);	
	       }	       
	       
	        // putting the File in FileOutPutStream();
	        FileOutputStream fileOut = new FileOutputStream(statisticFile);
	        workbook.write(fileOut);
	        fileOut.flush();
	        fileOut.close();
	        workbook.close();
	        
	        return statisticFile;
	        
		} catch (FileNotFoundException e) {
			Logger.error("Statistic file exception ", e );			
		} catch (IOException e) {
			Logger.error("IO exception", e);
		}  
		return null;
	}
	
	
	/**
	 * Used by methods  > createStatisticsFile() & showStats();
	 * This method applies the view for the statsproduct.html page and the excel file;
	 * What it actually does is makes the representation of all the data that is 
	 * show to String values;
	 * @param currentUser
	 */
	public static void applyTheStringViewForStats(User currentUser){ 
       // we find the list of products for which we want to generate the excel file with the statistics;
        allProducts = ProductController.findProduct.where().eq("owner.username", currentUser.username).findList();
        // Going through the products and setting the displaying the adequate Strings on stats page;
		Iterator<Product> iter = allProducts.iterator();
	
		while (iter.hasNext()) {
				Product p = iter.next();
				if (p.isSold) {
					p.statsProducts.setIsItSold("DA");
				} else {
					p.statsProducts.setIsItSold("NE");
				}
				if (p.isSpecial) {
					p.statsProducts.setIsItSpecial("DA");
				} else {
					p.statsProducts.setIsItSpecial("NE");
				}
			p.statsProducts.setNoOfClicksS(String.valueOf(p.statsProducts.noOfClicks));
			p.statsProducts.setNoOfComments(p.statsProducts.noOfComments);
			p.save();
		}
		
		
	}
	
}
