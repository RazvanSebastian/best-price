package com.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.model.Laptop;
import com.model.LaptopRetailer;
import com.model.Phone;
import com.model.PhoneRetailer;
import com.model.Product;
import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;
import com.model.Retailer;
import com.repository.LaptopRepository;
import com.repository.LaptopRetailerRepository;
import com.repository.PhoneRepository;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;

@Service("emagInspector")
public class EmagInspectorService implements ProductInspectorService {
	@Autowired
	private PhoneRepository phoneRepository;
	@Autowired
	private RetailerRepository retailerRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;
	@Autowired
	private LaptopRepository laptopRepository;
	@Autowired
	private LaptopRetailerRepository laptopRetailerRepository;

	private Document receivePageByUrl(String url) {
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getProductImage(Element productHolder) {
		return productHolder.getElementsByTag("img").attr("data-src");
	}

	private String getProductTitle(Element productHolder, String productType) {
		switch (productType) {
		case "phone":
			return productHolder.getElementsByClass("middle-container").text().substring(14);
		case "laptop":
			return productHolder.getElementsByClass("middle-container").text();
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
	
	private String getRetailerUrlOfProductOffer(Element productHolder){
		Element a=productHolder.getElementsByClass("middle-container").first().getElementsByAttribute("href").first();
		return "http://www.emag.ro"+a.attr("href");
	}

	/**
	 * @Input Element productHolder (generic name of div which contain all details about product)
	 * @Return String vector: index 0 = price , index 1 = currency
	 */
	private String[] getProductPriceAndCurrency(Element productHolder) {
		String[] productPriceCurrency = new String[2];
		Iterator<Element> iteretor = productHolder.getElementsByClass("price-over").iterator();
		String moneyDecimal;
		String moneyInteger;
		if (iteretor.hasNext()) {
			Element el = iteretor.next();
			moneyInteger = el.getElementsByClass("money-int").text();
			moneyDecimal = el.getElementsByClass("money-decimal").text();
			if(moneyDecimal.length()>=2)
				moneyDecimal = new BigDecimal(moneyDecimal).toPlainString().substring(0, 1);
			else
				moneyDecimal="0";
			if (moneyInteger.length() <= 3)
				productPriceCurrency[0] = moneyInteger + "." + moneyDecimal;
			if (moneyInteger.length() > 3 && moneyInteger.length() <= 7) {
				String[] price = moneyInteger.split(Pattern.quote("."));
				String hundredths = price[0];
				String dozens = price[1];
				productPriceCurrency[0] = hundredths + "" + dozens + "." + moneyDecimal;
			}
			if (moneyInteger.length() > 7) {
				String[] price = moneyInteger.split(Pattern.quote("."));
				String thousands = price[0];
				String hundredths = price[1];
				String dozens = price[2];
				productPriceCurrency[0] = thousands + "" + hundredths + "" + dozens + "." + moneyDecimal;

			}
			productPriceCurrency[1] = el.getElementsByClass("money-currency").text();
		}


		return productPriceCurrency;
	}

	private Stock getProductStokState(Element productHolder) {
		String stockState = productHolder.getElementsByClass("stare-disp-listing").text();
		if (stockState.equals("In stoc"))
			return Stock.InStock;
		if (stockState.equals("Stoc limitat"))
			return Stock.LimitedStock;
		if (stockState.equals("Stoc epuizat"))
			return Stock.OutOfStock;
		return Stock.Soon;
	}

	private Phone setNewPhoneAttributes(Element productHolder) {
		Phone newPhone = new Phone();
		// new phone attributes set
		newPhone.setImage(this.getProductImage(productHolder));
		newPhone.setTitle(this.getProductTitle(productHolder, "phone"));
		newPhone.setRating(this.getProductRating(productHolder));
		newPhone.setReviews(this.getProductReview(productHolder));
		return newPhone;
	}

	private PhoneRetailer setNewPhoneRetailerAttributes(Element productHolder, Phone newPhone, Retailer retailer) {
		String[] string = new String[2];
		PhoneRetailer phoneRetailer = new PhoneRetailer();
		// new phone-retailer attributes set
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
		phoneRetailer.setRetailerOfferUrl(this.getRetailerUrlOfProductOffer(productHolder));
		return phoneRetailer;
	}

	private Laptop setNewLaptopAttributes(Element productHolder) {
		Laptop newLaptop = new Laptop();
		// new phone attributes set
		newLaptop.setImage(this.getProductImage(productHolder));
		newLaptop.setTitle(this.getProductTitle(productHolder, "laptop"));
		newLaptop.setRating(this.getProductRating(productHolder));
		newLaptop.setReviews(this.getProductReview(productHolder));
		return newLaptop;
	}

	private LaptopRetailer setNewLaptopRetailerAttributes(Element productHolder, Laptop newLaptop, Retailer retailer) {
		String[] string = new String[2];
		LaptopRetailer laptopRetailer = new LaptopRetailer();
		// new phone-retailer attributes set
		string = this.getProductPriceAndCurrency(productHolder);
		laptopRetailer.setPrice(Double.parseDouble(string[0].split(" ")[0]));
		if (string[1].equals("Lei"))
			laptopRetailer.setMoneyCurrency(MoneyCurrency.Lei);
		if (string[1].equals("Euros"))
			laptopRetailer.setMoneyCurrency(MoneyCurrency.Euros);
		if (string[1].equals("Dollars"))
			laptopRetailer.setMoneyCurrency(MoneyCurrency.Dollars);
		laptopRetailer.setStock(this.getProductStokState(productHolder));
		laptopRetailer.setLastDateCheck(new Date());
		laptopRetailer.setLaptop(newLaptop);
		laptopRetailer.setRetailer(retailer);
		laptopRetailer.setRetailerOfferUrl(this.getRetailerUrlOfProductOffer(productHolder));
		return laptopRetailer;
	}

	/**
	 * 
	 * @param doc
	 *            , productType
	 * @return List of products as generic
	 */
	private List<Product> getAllProductFromPage(Document doc, String productType) {
		List<Product> productList = new ArrayList<Product>();
		String[] string = new String[2];

		// retailer emag
		Retailer retailer = new Retailer();
		retailer = this.retailerRepository.findRetailerByName("Emag");

		// new phone
		Phone newPhone;
		// retailer-phone intermediate table
		PhoneRetailer phoneRetailer;

		// new laptop
		Laptop newLaptop;
		// retailer-laptop
		LaptopRetailer laptopRetailer;

		Elements phonePageContainers = doc.getElementsByClass("product-holder-grid");
		if (productType.equals("phone")) {
			for (Element productHolder : phonePageContainers) {

				newPhone = new Phone();
				newPhone = this.setNewPhoneAttributes(productHolder);
				this.phoneRetailerRepository
						.save(this.setNewPhoneRetailerAttributes(productHolder, newPhone, retailer));
				// new phone added
				productList.add(newPhone);
			}
		}
		if (productType.equals("laptop")) {
			for (Element productHolder : phonePageContainers) {
				newLaptop = new Laptop();
				newLaptop = this.setNewLaptopAttributes(productHolder);
				this.laptopRetailerRepository
						.save(this.setNewLaptopRetailerAttributes(productHolder, newLaptop, retailer));
			}
		}
		return productList;
	}

	/**
	 * @param string
	 *            of URL specific for product
	 * @return number of product pages with a specific product
	 */
	private int getPageNumbers(String productTypePage) {
		try {
			Document doc = Jsoup.connect(productTypePage).get();
			return Integer.parseInt(doc.getElementsByClass("emg-pagination-no").last().text());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Transactional
	public void initializePhoneTable() {
		int pageNumbers = this.getPageNumbers("http://www.emag.ro/telefoane-mobile/p1/c");
		List<Phone> phones = new ArrayList<Phone>();
		List<Product> products = new ArrayList<Product>();
		for (int i = 1; i <= pageNumbers; i++) {
			products.addAll(this.getAllProductFromPage(
					this.receivePageByUrl("http://www.emag.ro/telefoane-mobile/p" + i + "/c"), "phone"));
		}

		for (Product prod : products)
			phones.add((Phone) prod);
		this.phoneRepository.save(phones);
	}

	@Transactional
	public void initializeLaptopTable() {
		int pageNumbers = this.getPageNumbers("http://www.emag.ro/laptopuri/p1/c");
		List<Product> products = new ArrayList<Product>();
		List<Laptop> laptops = new ArrayList<Laptop>();
		for (int i = 1; i <= pageNumbers; i++) {
			products.addAll(this.getAllProductFromPage(
					this.receivePageByUrl("http://www.emag.ro/laptopuri/p" + i + "/c"), "laptop"));
		}

		for (Product prod : products)
			laptops.add((Laptop) prod);
		this.laptopRepository.save(laptops);
	}
}
