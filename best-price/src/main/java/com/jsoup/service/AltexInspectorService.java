package com.jsoup.service;

import static org.mockito.Matchers.doubleThat;

import java.io.IOException;
import java.util.ArrayList;
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

import ch.qos.logback.core.boolex.Matcher;

@Component
public class AltexInspectorService extends RetailerInspector {

	@Autowired
	private PhoneRepository phoneRepository;
	@Autowired
	private PhoneBrandRepository phoneBrandRepository;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PhoneRetailer setNewPhoneRetailerAttributes(Element productHolder, Phone newPhone, Retailer retailer) {
		// TODO Auto-generated method stub
		return null;
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

		return null;
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

	private void checkForDuplicateByNameAndBrand(String brandName, String phoneName) {
		PhoneBrand brand = this.phoneBrandRepository.findByBrandName(brandName);
	}

	@Scheduled(cron = "*/5 * * * * *")
	private void test() {

		List<BrandDao> phoneBrands = this.getBrandsByProduct("phone");
		for (BrandDao brand : phoneBrands) {
			// if the brand is new add in
			System.out.println(brand.getBrandName());
			for (int i = 1; i <= brand.getNumberOfPages(); i++) {
				Elements productHolders = this.receivePageByUrl(brand.getUrlFirstPage() + "/p/" + i)
						.getElementsByClass("Product");

				for (Element productHolder : productHolders) {
					String title = this.getProductTitle(productHolder, "phone").replaceAll(",", "");
					System.out.println(title);
					if (!title.equals("titile not known") && title.split(brand.getBrandName()).length>0) {
						String titleWihoutName = title.split(brand.getBrandName())[1];

						if (titleWihoutName.split("\\s").length >= 6) {
							String[] atrs = titleWihoutName.split("\\s");
							String atr1 = atrs[0].toLowerCase();
							String atr2 = atrs[1].toLowerCase();
							String atr3 = atrs[2].toLowerCase();
							String lastAtr1 = atrs[atrs.length - 2].toLowerCase();
							String lastAtr2 = atrs[atrs.length - 1].toLowerCase();
							System.out.println(titleWihoutName + " : "
									+ this.phoneRepository.findBy5Attributes(atr1, atr2, atr3, lastAtr1, lastAtr2));
						}
					}
				}
			}
		}
	}

}
