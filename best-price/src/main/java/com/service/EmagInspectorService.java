package com.service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.Phone;
import com.model.PhoneRetailer;
import com.model.PhoneRetailer.MoneyCurrency;
import com.model.PhoneRetailer.Stock;
import com.model.Product;
import com.model.Retailer;
import com.repository.PhoneRepository;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;


@Service("emagInspector")
public class EmagInspectorService implements ProductInspectorService{
	@Autowired 
	private PhoneRepository phoneRepository;
	@Autowired
	private RetailerRepository retailerRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;

	private Document receivePageByUrl(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			return doc;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getProductImage(Element productHolder) {
		return productHolder.getElementsByTag("img").attr("data-src");
	}

	private String getProductTitle(Element productHolder,String productType) {
		switch(productType){
			case "phone":return productHolder.getElementsByClass("middle-container").text().substring(14);
			case "laptop":return productHolder.getElementsByClass("middle-container").text();
		}
		return "";
	}

	private Double getProductRating(Element productHolder) {
		String ratingString = productHolder.getElementsByClass("star-rating-small-progress").attr("style");
		if (ratingString.length() > 0)
			return Double.parseDouble(ratingString.substring(6, ratingString.length() - 1).toString());
		else
			return 0.0;
	}

	private int getProductReview(Element productHolder) {
		String reviewString = productHolder.getElementsByClass("holder-rating").text();
		if (reviewString.length() > 1)
			return Integer
					.parseInt(productHolder.getElementsByClass("holder-rating").text().split(" ")[0].substring(1));
		else
			return 0;
	}

	/**
	 * @Input Object of element type
	 * @Return String vector: index 0 = price , index 1 = currency
	 */
	private String[] getProductPriceAndCurrency(Element productHolder) {
		String[] productPriceCurrency = new String[2];
		Iterator<Element> iteretor = productHolder.getElementsByClass("price-over").iterator();

		if (iteretor.hasNext()) {
			Element el = iteretor.next();
			productPriceCurrency[0] = el.getElementsByClass("money-int").text();
			productPriceCurrency[1] = el.getElementsByClass("money-currency").text();
		}

		return productPriceCurrency;
	}

	private Stock getProductStokState(Element productHolder) {
		String stockState = productHolder.getElementsByClass("stare-disp-listing").text();
		if(stockState.equals("In stoc"))
			return Stock.InStock;
		if(stockState.equals("Stoc limitat"))
			return Stock.LimitedStock;
		if(stockState.equals("Stoc epuizat"))
			return Stock.OutOfStock;
		return Stock.Soon;
	}
	
	/**
	 * 
	 * @param doc , productType
	 * @return List of products as generic
	 */
	private List<Product> getAllProductFromPage(Document doc,String productType) {
		List<Product> productList = new ArrayList<Product>();
		String[] string = new String[2];
		//new phone
		Phone newPhone;
		//retailer emag
		Retailer retailer=new Retailer();
		retailer=this.retailerRepository.findRetailerByName("Emag");
		//retailer-phone intermediate table
		PhoneRetailer phoneRetailer;
		
		Elements phonePageContainers = doc.getElementsByClass("product-holder-grid");
		for (Element productHolder : phonePageContainers) {

			newPhone = new Phone();
			phoneRetailer=new PhoneRetailer();
			//new phone attributes set
			newPhone.setImage(this.getProductImage(productHolder));
			newPhone.setTitle(this.getProductTitle(productHolder,productType));
			newPhone.setRating(this.getProductRating(productHolder));
			newPhone.setReviews(this.getProductReview(productHolder));
			
			//new phone-retailer attributes set
			string = this.getProductPriceAndCurrency(productHolder);
			phoneRetailer.setPrice(Double.parseDouble(string[0].split(" ")[0]));
			if (string[1].equals("Lei"))
				phoneRetailer.setMoneyCurrency(MoneyCurrency.Lei);
			if (string[1].equals("Euros"))
				phoneRetailer.setMoneyCurrency(MoneyCurrency.Euros);
			if (string[1].equals("Dollars"))
				phoneRetailer.setMoneyCurrency(MoneyCurrency.Dollars);
			
			phoneRetailer.setStock(this.getProductStokState(productHolder));
			phoneRetailer.setLastDateCheck(new Date());
			phoneRetailer.setPhone(newPhone);
			phoneRetailer.setRetailer(retailer);
			this.phoneRetailerRepository.save(phoneRetailer);
			
			//new phone added
			productList.add(newPhone);
		
		}
		return productList;
	}
	
	/**
	 * @param string of URL specific for product
	 * @return number of product pages with a specific product 
	 * */
	private int getPageNumbers(String productTypePage){
		try {
			Document doc=Jsoup.connect(productTypePage).get();
			return Integer.parseInt(doc.getElementsByClass("emg-pagination-no").last().text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	@Transactional
	public void initializePhoneTable() {
			int pageNumbers=this.getPageNumbers("http://www.emag.ro/telefoane-mobile/p1/c");
			List<Phone> phones=new ArrayList<Phone>();
			List<Product> products=new ArrayList<Product>();
			for(int i=1;i<=pageNumbers;i++){
				products.addAll(this.getAllProductFromPage(this.receivePageByUrl("http://www.emag.ro/telefoane-mobile/p"+i+"/c"), "phone"));
			}
			
			for(Product prod:products)
				phones.add((Phone)prod);
			this.phoneRepository.save(phones);
	}
	@Transactional
	public void initializeLaptopTable(){
		
	}
}
