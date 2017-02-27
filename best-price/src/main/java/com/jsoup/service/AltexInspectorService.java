package com.jsoup.service;

import static org.mockito.Matchers.doubleThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.model.Phone;
import com.model.Product;
import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;
import com.repository.PhoneRepository;

@Service
public class AltexInspectorService {
	
	@Autowired
	private PhoneRepository phoneRepositroy;

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

	private int getPageNumb(Element el) {

		Element pager = el.getElementsByClass("u-display-iblock Toolbar-pager").last();
		String stringValue = pager.getElementsByAttribute("selected").text().split("/")[1].substring(1);
		int numbOfPages = Integer.parseInt(stringValue);
		return numbOfPages;
	}

	private String getProductName(Element productContainer) {
		return productContainer.getElementsByClass("Product-photoTrigger").first().attr("title");
	}

	private String getProductUrlImage(Element productContainer) {
		return productContainer.getElementsByClass("Product-photo").first().attr("src");
	}

	private String getRetailerOfferUrl(Element productContainer) {
		return productContainer.getElementsByClass("Product-photoTrigger").first().attr("href");
	}
	
	private double getPriceAndCurrency(Element productContainer){
		String price=productContainer.getElementsByAttributeValue("itemprop", "price").first().attr("content");
		return Double.parseDouble(price);
	}
	
	private MoneyCurrency getCurrency(Element productContainer){
		String currency=productContainer.getElementsByAttributeValue("itemprop", "priceCurrency").first().attr("content");
		switch(currency){
		case "RON":return MoneyCurrency.Lei;
		default : return MoneyCurrency.Euros;
		}
	}
	
	private Stock getStockStatus(Element productContainer){
		String stockStatus=productContainer.getElementsByClass("Status").text();
		switch(stockStatus){
		case "In stoc":return Stock.InStock;
		case "Stoc epuizat":return Stock.OutOfStock;
		default : return Stock.Soon;
		}
	}
	
//	private List<Product> getProductDetails( ) {
//		
//		Document doc = this.receivePageByUrl("http://altex.ro/telefoane-tablete-si-gadgets/smartphone/filtru/p/3");
//		Element productsContainer = doc.getElementsByClass("lg-u-float-right lg-u-size-8of10").first();
//	
//		Elements phonePage = productsContainer.getElementsByClass("Product");
//	
//		List<Product> newProducts=new ArrayList<>();
//		Phone newPhone;
//		
//		for(Element el:phonePage){
//			newPhone=new Phone();
//			newPhone.setTitle(this.getProductName(el));
//			newPhone.setImage(this.getProductUrlImage(el));
//			this.phoneRepositroy.save(newPhone);
//		}
//
//	}
	
	

}
