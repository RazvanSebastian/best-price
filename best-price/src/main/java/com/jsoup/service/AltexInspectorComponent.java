package com.jsoup.service;

import static org.mockito.Matchers.doubleThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dao.BrandDao;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.local.components.PhoneAttributesHandler;
import com.model.Laptop;
import com.model.LaptopRetailer;
import com.model.Phone;
import com.model.PhoneBrand;
import com.model.PhoneRetailer;
import com.model.Product;
import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;
import com.model.Retailer;
import com.repository.PhoneBrandRepository;
import com.repository.PhoneRepository;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;

import ch.qos.logback.core.boolex.Matcher;
import org.apache.commons.lang.StringUtils;

@Component("altexInspector")
public class AltexInspectorComponent extends RetailerInspectorService {

	@Autowired
	private PhoneRepository phoneRepository;
	@Autowired
	private PhoneBrandRepository phoneBrandRepository;
	@Autowired
	private PhoneAttributesHandler phoneAttributesHandler;
	@Autowired
	private RetailerRepository retailerRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;

	@Override
	protected Document receivePageByUrl(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			return doc;
		} catch (IOException e) {
			this.receivePageByUrl(url);
		}
		return null;
	}

	@Override
	public int getnumberOfPages(String productTypePage) {
		Document doc = this.receivePageByUrl(productTypePage);
		String numberOfPages = doc.getElementsByClass("u-display-iblock Toolbar-pager").first()
				.getElementsByClass("js-trigger-catalog-toolbar-apply-filters").first().getElementsByTag("option")
				.first().text();
		System.out.println(numberOfPages);
		if (numberOfPages.length() == 5)
			return Integer.parseInt(numberOfPages.substring(numberOfPages.length() - 1));
		if (numberOfPages.length() == 6)
			return Integer.parseInt(numberOfPages.substring(numberOfPages.length() - 2));
		return 1;

	}

	@Override
	public String getProductImage(Element productHolder) {
		return productHolder.getElementsByClass("Product-photoWrapper").first().getElementsByTag("img").first()
				.attr("src");
	}

	@Override
	public String getProductTitle(Element productHolder, String productType) {
		String title = productHolder.getElementsByClass("Product-photoTrigger").first().attr("title");
		switch (productType) {
		case "phone":
			if (title.startsWith("Smartphone"))
				return title.substring(11);
		case "laptop":
			if (title.startsWith("Laptop"))
				return title.substring(7);
		default:
			return "titile not known";
		}
	}

	@Override
	public Double getProductRating(Element productHolder) {
		Element el = this.receivePageByUrl(this.getRetailerUrlOfProductOffer(productHolder));
		return Double.parseDouble(el.getElementsByClass("Rating-value").first().attr("style").split("[\\:\\%]")[1]);

	}

	@Override
	public int getProductReview(Element productHolder) {
		Element el = this.receivePageByUrl(this.getRetailerUrlOfProductOffer(productHolder));
		String reviewCount = el.getElementsByClass("Rating-reviews js-gotoHash").first().text();
		// if the length is more then 10 means that there is text
		if (reviewCount.length() > 10)
			return 0;
		else
			return Integer.parseInt(reviewCount);
	}

	@Override
	public String getRetailerUrlOfProductOffer(Element productHolder) {
		return productHolder.getElementsByClass("Product-photoTrigger").first().attr("href");
	}

	@Override
	public Stock getProductStokState(Element productHolder) {
		String status = productHolder.getElementsByClass("Status").first().text();
		switch (status.toLowerCase()) {
		case "in stoc":
			return Stock.InStock;
		case "stoc limitat":
			return Stock.LimitedStock;
		case "stoc epuizat":
			return Stock.OutOfStock;
		default:
			return Stock.Soon;
		}
	}

	@Override
	public String getProductPrice(Element productHolder) {
		return productHolder.getElementsByClass("Product-list-right").first().getElementsByTag("meta").get(0)
				.attr("content");

	}

	@Override
	public MoneyCurrency getProductCurrency(Element productHolder) {
		String currency = productHolder.getElementsByClass("Product-list-right").first().getElementsByTag("meta").get(1)
				.attr("content");
		switch (currency.toLowerCase()) {
		case "ron":
			return MoneyCurrency.Lei;
		default:
			return MoneyCurrency.Euros;
		}
	}

	@Override
	protected Phone setNewPhoneAttributes(Element productHolder) {
		Phone newPhone=new Phone();
		newPhone.setImage(this.getProductImage(productHolder));
		newPhone.setTitle(this.getProductTitle(productHolder, "phone"));
		return newPhone;
	}

	@Override
	protected PhoneRetailer setNewPhoneRetailerAttributes(Element productHolder, Phone newPhone, Retailer retailer) {
		PhoneRetailer newPhoneRetailer=new PhoneRetailer();
		newPhoneRetailer.setLastDateCheck(new Date());
		newPhoneRetailer.setMoneyCurrency(this.getProductCurrency(productHolder));
		newPhoneRetailer.setPrice(Double.parseDouble(this.getProductPrice(productHolder)));
		newPhoneRetailer.setRating(this.getProductRating(productHolder));
		newPhoneRetailer.setRetailerOfferUrl(this.getRetailerUrlOfProductOffer(productHolder));
		newPhoneRetailer.setReviews(this.getProductReview(productHolder));
		newPhoneRetailer.setStock(this.getProductStokState(productHolder));
		newPhoneRetailer.setPhone(newPhone);
		newPhoneRetailer.setRetailer(retailer);
		return newPhoneRetailer;
	}

	@Override
	protected Laptop setNewLaptopAttributes(Element productHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LaptopRetailer setNewLaptopRetailerAttributes(Element productHolder, Laptop newLaptop,
			Retailer retailer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Product> getAllProductFromPage(Document doc, String productType) {

		return null;
	}

	@Override
	public List<BrandDao> getBrandsByProduct(String productType) {
		Elements ulBrands = null;
		switch (productType.toLowerCase()) {
		case "phone":
			ulBrands = this.receivePageByUrl("http://altex.ro/telefoane-tablete-si-gadgets/smartphone")
					.getElementsByClass("filter-options-container").get(3).getElementsByClass("filter-option");
			break;
		case "laptop":
			ulBrands = this.receivePageByUrl("http://altex.ro/laptop-it/laptopuri")
					.getElementsByClass("filter-options-container").get(3).getElementsByClass("filter-option");
			break;
		default:
			ulBrands = this.receivePageByUrl("http://altex.ro/telefoane-tablete-si-gadgets/smartphone")
					.getElementsByClass("filter-options-container").get(3).getElementsByClass("filter-option");
			break;
		}

		List<BrandDao> productBrands = new ArrayList<BrandDao>();
		String brandName;
		String url;
		int numberOfProducts;
		int numberOfPages;
		for (Element liBrand : ulBrands) {
			brandName = liBrand.text().split(" ")[0];
			url = liBrand.getElementsByTag("a").attr("href");
			numberOfProducts = Integer.parseInt(liBrand.text().split("[\\(\\)]")[1]);
			numberOfPages = (numberOfProducts / 48) + (numberOfProducts % 48 == 0 ? 0 : 1);
			productBrands.add(new BrandDao(brandName, url, numberOfPages));
		}
		return productBrands;
	}

	@Override

	public List<Phone> getAllRetailerPhones() {
		Retailer altexRetailer=this.retailerRepository.findRetailerByName("Altex");
		List<BrandDao> phoneBrands = this.getBrandsByProduct("phone");
		List<Phone> newPhoneList = new ArrayList<>();
		for (BrandDao brand : phoneBrands) {
			
			PhoneBrand phoneBrand=this.phoneBrandRepository.findByBrandName(brand.getBrandName());

			if(phoneBrand==null)
				phoneBrand=this.phoneBrandRepository.save(new PhoneBrand(brand.getBrandName()));
			System.out.println(phoneBrand.getBrandName());
			for (int i = 1; i <= brand.getNumberOfPages(); i++) {
				Elements productHolders = this.receivePageByUrl(brand.getUrlFirstPage() + "/p/" + i)
						.getElementsByClass("Product");

				for (Element productHolder : productHolders) {
					String title = this.getProductTitle(productHolder, "phone").replaceAll(",", "");

					if (!title.equals("titile not known")) {
						String[] titleAux = title.split(brand.getBrandName());
						String titleWihoutName;
						//check if we have in title the string brand name, if we have it we are removing it searching after by attributes
						if (titleAux.length == 2)
							titleWihoutName = titleAux[1].replaceAll("^\\s+", "").replaceAll("\\s+$", "");
						else
							titleWihoutName = titleAux[0].replaceAll("^\\s+", "").replaceAll("\\s+$", "");
						//if the title has more than 3 words we are checking after phone in phone table 
						if (titleWihoutName.split(" ").length >= 3) {
							List<Phone> resultList;
							String[] attrs = this.phoneAttributesHandler.getDetailsOfTitlePhone(titleWihoutName,
									titleWihoutName.split("\\s").length);
							resultList = this.phoneRepository.findAllPhoneLikeAttributes(attrs[0], attrs[1], attrs[2],
									attrs[3], attrs[4], attrs[5], attrs[6]);
							//check if we have zero, one or more phone with some attributes; if we have we are choosing the phone with string title with minimal size
							Phone phone = this.phoneAttributesHandler
									.checkResultsAndGetPhoneCorespodingToDB(resultList);
							//if the phone is null mean that we haven't this phone in data base yet; we add it
							if(phone==null){
								Phone newPhone=this.setNewPhoneAttributes(productHolder);
								newPhone.setPhoneBrand(phoneBrand);
								newPhone=this.phoneRepository.save(newPhone);
								newPhoneList.add(newPhone);
								PhoneRetailer newPhoneRetailer = this.setNewPhoneRetailerAttributes(productHolder, newPhone, altexRetailer);
								newPhoneRetailer.setPhone(newPhone);
								newPhoneRetailer.setRetailer(altexRetailer);
								this.phoneRetailerRepository.save(newPhoneRetailer);
							}
							else{
								PhoneRetailer newPhoneRetailer = this.setNewPhoneRetailerAttributes(productHolder, phone, altexRetailer);
								newPhoneRetailer.setPhone(phone);
								newPhoneRetailer.setRetailer(altexRetailer);
								this.phoneRetailerRepository.save(newPhoneRetailer);
							}
						}
						else{
							Phone newPhone=this.setNewPhoneAttributes(productHolder);
							newPhone.setPhoneBrand(phoneBrand);
							newPhone=this.phoneRepository.save(newPhone);
							newPhoneList.add(newPhone);
							PhoneRetailer newPhoneRetailer = this.setNewPhoneRetailerAttributes(productHolder, newPhone, altexRetailer);
							newPhoneRetailer.setPhone(newPhone);
							newPhoneRetailer.setRetailer(altexRetailer);
							this.phoneRetailerRepository.save(newPhoneRetailer);
						}
					}
					
				}
			}
		}
		return newPhoneList;
	}

	@Override
	public List<Laptop> getAllRetailerLaptops() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double inspectPhonePriceByUrl(String urlPhonePage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Scheduled(cron = "*/5 * * * * *")
	private void test() {

	
	}
}
