package com.jsoup.service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dao.BrandDao;
import com.model.Laptop;
import com.model.LaptopBrand;
import com.model.LaptopRetailer;
import com.model.Phone;
import com.model.PhoneBrand;
import com.model.PhoneRetailer;
import com.model.Product;
import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;
import com.model.Retailer;
import com.repository.LaptopBrandRepository;
import com.repository.LaptopRetailerRepository;
import com.repository.PhoneBrandRepository;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;

@Component("emagInspector")
public class EmagInspectorService extends RetailerInspector {
	@Autowired
	private RetailerRepository retailerRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;
	@Autowired
	private LaptopRetailerRepository laptopRetailerRepository;
	@Autowired
	private PhoneBrandRepository phoneBrandRepository;
	@Autowired
	private LaptopBrandRepository laptopBrandRepository;

	public Document receivePageByUrl(String url) {
		Connection.Response response = null;
		try {
			response = Jsoup.connect(url).method(Connection.Method.GET).execute();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Document doc;
		try {
			doc = Jsoup.connect(url).cookies(response.cookies()).get();
			return doc;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getProductImage(Element productHolder) {
		return productHolder.getElementsByTag("img").attr("data-src");
	}

	public String getProductTitle(Element productHolder, String productType) {
		switch (productType) {
		case "phone":
			return productHolder.getElementsByClass("middle-container").text().substring(14);
		case "laptop":
			return productHolder.getElementsByClass("middle-container").text();
		}
		return "";
	}

	public Double getProductRating(Element productHolder) {
		String ratingString = productHolder.getElementsByClass("star-rating-small-progress").attr("style");
		if (ratingString.length() > 0)
			return Double.parseDouble(ratingString.substring(6, ratingString.length() - 1).toString());
		else
			return 0.0;
	}

	public int getProductReview(Element productHolder) {
		String reviewString = productHolder.getElementsByClass("holder-rating").text();
		if (reviewString.length() > 1)
			return Integer
					.parseInt(productHolder.getElementsByClass("holder-rating").text().split(" ")[0].substring(1));
		else
			return 0;
	}

	public String getRetailerUrlOfProductOffer(Element productHolder) {
		Element a = productHolder.getElementsByClass("middle-container").first().getElementsByAttribute("href").first();
		return "http://www.emag.ro" + a.attr("href");
	}

	public String getProductPrice(Element productHolder) {
		String moneyInteger = productHolder.getElementsByClass("price-over").first().getElementsByClass("money-int")
				.text();
		return moneyInteger;
	}

	public String handleProductPrice(String moneyInteger) {

		String productPrice = "";
		if (moneyInteger.length() <= 3)
			productPrice = moneyInteger;
		if (moneyInteger.length() > 3 && moneyInteger.length() <= 7) {
			String[] price = moneyInteger.split(Pattern.quote("."));
			String hundredths = price[0];
			String dozens = price[1];
			productPrice = hundredths + "" + dozens;
		}
		if (moneyInteger.length() > 7) {
			String[] price = moneyInteger.split(Pattern.quote("."));
			String thousands = price[0];
			String hundredths = price[1];
			String dozens = price[2];
			productPrice = thousands + "" + hundredths + "" + dozens;
		}
		return productPrice;

	}

	/**
	 * @Input Element productHolder (generic name of div which contain all
	 *        details about product)
	 * @Return String vector: index 0 = price , index 1 = currency
	 */

	@Override
	public MoneyCurrency getProductCurrency(Element productHolder) {
		// TODO Auto-generated method stub
		String currency = productHolder.getElementsByClass("price-over").first().getElementsByClass("money-currency")
				.text();
		switch (currency.toLowerCase()) {
		case "lei":
			return MoneyCurrency.Lei;
		default:
			return MoneyCurrency.Euros;
		}
	}

	public Stock getProductStokState(Element productHolder) {
		String stockState = productHolder.getElementsByClass("stare-disp-listing").text();
		if (stockState.equals("In stoc"))
			return Stock.InStock;
		if (stockState.equals("Stoc limitat"))
			return Stock.LimitedStock;
		if (stockState.equals("Stoc epuizat"))
			return Stock.OutOfStock;
		return Stock.Soon;
	}

	public Phone setNewPhoneAttributes(Element productHolder) {
		Phone newPhone = new Phone();
		// new phone attributes set
		newPhone.setImage(this.getProductImage(productHolder));
		newPhone.setTitle(this.getProductTitle(productHolder, "phone"));
		return newPhone;
	}

	public PhoneRetailer setNewPhoneRetailerAttributes(Element productHolder, Phone newPhone, Retailer retailer) {
		String[] string = new String[2];
		PhoneRetailer phoneRetailer = new PhoneRetailer();
		// new phone-retailer attributes set
		phoneRetailer.setPhone(newPhone);
		phoneRetailer.setRetailer(retailer);
		// details
		phoneRetailer.setPrice(Double.parseDouble(this.handleProductPrice(this.getProductPrice(productHolder))));
		phoneRetailer.setMoneyCurrency(this.getProductCurrency(productHolder));

		phoneRetailer.setStock(this.getProductStokState(productHolder));
		phoneRetailer.setLastDateCheck(new Date());
		phoneRetailer.setRetailerOfferUrl(this.getRetailerUrlOfProductOffer(productHolder));
		phoneRetailer.setReviews(this.getProductReview(productHolder));
		phoneRetailer.setRating(this.getProductRating(productHolder));
		return phoneRetailer;
	}

	public Laptop setNewLaptopAttributes(Element productHolder) {
		Laptop newLaptop = new Laptop();
		// new phone attributes set
		newLaptop.setImage(this.getProductImage(productHolder));
		newLaptop.setTitle(this.getProductTitle(productHolder, "laptop"));
		newLaptop.setRating(this.getProductRating(productHolder));
		newLaptop.setReviews(this.getProductReview(productHolder));
		return newLaptop;
	}

	public LaptopRetailer setNewLaptopRetailerAttributes(Element productHolder, Laptop newLaptop, Retailer retailer) {
		String[] string = new String[2];
		LaptopRetailer laptopRetailer = new LaptopRetailer();
		// new phone-retailer attributes set

		laptopRetailer.setPrice(Double.parseDouble(this.handleProductPrice(this.getProductPrice(productHolder))));
		laptopRetailer.setMoneyCurrency(this.getProductCurrency(productHolder));
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
	public List<Product> getAllProductFromPage(Document doc, String productType) {
		List<Product> productList = new ArrayList<Product>();
		// retailer emag
		Retailer retailer = new Retailer();
		retailer = this.retailerRepository.findRetailerByName("Emag");

		// new phone
		Phone newPhone;
		// new laptop
		Laptop newLaptop;
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
				productList.add(newLaptop);
			}
		}
		return productList;
	}

	/**
	 * @param string
	 *            of URL specific for product
	 * @return number of product pages with a specific product
	 */
	public int getnumberOfPages(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			String text = doc.getElementsByClass("emg-pagination-box").last().text();

			if (!text.equals("Afisare") && text.length() > 1) {
				text = text.substring(text.length() - 1);
				return Integer.parseInt(text);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.getnumberOfPages(url);
			e.printStackTrace();
		}
		return 0;
	}

	// ---------------------------------------------------------------------
	// --------------PUBLIC METHODS----------------------------------------
	// --------------------------------------------------------------------

	public List<BrandDao> getBrandsByProduct(String productType) {
		List<BrandDao> daoBrands = new ArrayList<>();

		Document doc;
		switch (productType) {
		case "phone":
			doc = this.receivePageByUrl("http://www.emag.ro/telefoane-mobile/c");
			break;
		case "laptop":
			doc = this.receivePageByUrl("http://www.emag.ro/laptopuri/c");
			break;
		default:
			doc = this.receivePageByUrl("http://www.emag.ro/telefoane-mobile/c");
		}
		Element brandFilterList = doc.getElementsByClass("sticky-filters").first().getElementsByClass("filters-list")
				.get(2);

		Elements liBrands = brandFilterList.getElementsByTag("li");
		for (Element li : liBrands) {
			if (li.siblingIndex() != 10) {
				String text = li.text().replaceAll("\\u00A0", " ").trim();
				String title = text.split("\\(")[0];
				if (title.split(" ").length > 1)
					title = title.split(" ")[0] + "-" + title.split(" ")[1];
				int numberOfProducts = Integer.parseInt(li.text().split("[\\(\\)]")[1]);
				int numberOfPages = (numberOfProducts / 60) + (numberOfProducts % 60 == 0 ? 0 : 1);
				String url = "http://www.emag.ro/" + li.getElementsByTag("input").first().attr("data-url");
				daoBrands.add(new BrandDao(title, url.substring(0, url.lastIndexOf("/")), numberOfPages));
			}
		}
		return daoBrands;
	}

	public List<Phone> getAllRetailerPhones() {
		int numberOfPages;
		List<Phone> phones = new ArrayList<Phone>();
		List<Product> productsByBrand = null;
		List<PhoneBrand> brands = this.phoneBrandRepository.findAll();
		List<BrandDao> brandsDao = this.getBrandsByProduct("phone");
		for (PhoneBrand brand : brands) {
			System.out.println(brandsDao.get(brand.getIdPhoneBrand()-1).getUrlFirstPage());
			numberOfPages = brandsDao.get(brand.getIdPhoneBrand() - 1).getNumberOfPages();
			productsByBrand = new ArrayList<Product>();
			if (numberOfPages != 0) {
				for (int i = 1; i <= numberOfPages; i++) {
					String url = brandsDao.get(brand.getIdPhoneBrand() - 1).getUrlFirstPage();
					productsByBrand
							.addAll(this.getAllProductFromPage(this.receivePageByUrl(url + "/p" + i + "/c"), "phone"));
				}
				for (Product prod : productsByBrand) {
					phones.add((Phone) prod);
					phones.get(phones.size() - 1).setPhoneBrand(brand);
				}
			}
		}
		return phones;
	}

	public List<Laptop> getAllRetailerLaptops() {
		int numberOfPages;
		List<Laptop> laptops = new ArrayList<Laptop>();
		List<Product> productsByBrand = null;
		List<LaptopBrand> brands = this.laptopBrandRepository.findAll();
		List<BrandDao> brandsDao = this.getBrandsByProduct("laptop");

		for (LaptopBrand brand : brands) {
			numberOfPages = this.getnumberOfPages(
					"http://www.emag.ro/" + brandsDao.get(brand.getIdLaptopeBrand() - 1).getUrlFirstPage());
			productsByBrand = new ArrayList<Product>();

			for (int i = 1; i <= numberOfPages; i++) {
				String url = brandsDao.get(brand.getIdLaptopeBrand() - 1).getUrlFirstPage();
				productsByBrand.addAll(this.getAllProductFromPage(this.receivePageByUrl(
						"http://www.emag.ro/laptopuri/brand/" + url.substring(0, url.length() - 2) + "/p" + i + "/c"),
						"laptop"));
			}
			for (Product prod : productsByBrand) {
				laptops.add((Laptop) prod);
				laptops.get(laptops.size() - 1).setLaptopBrand(brand);

			}
		}
		return laptops;
	}

	public double inspectPhonePriceByUrl(String urlPhonePage) {
		Document doc = this.receivePageByUrl(urlPhonePage);
		Element priceElements = doc.getElementsByClass("product-new-price").first();
		String[] priceAndCurrency = priceElements.text().split(" ");
		if (priceAndCurrency[0].length() >= 2) {
			String integer = priceAndCurrency[0].substring(0, priceAndCurrency[0].length() - 2);
			priceAndCurrency[0].substring(priceAndCurrency[0].length() - 2);
			return Double.parseDouble(this.handleProductPrice(integer));
		}
		return 0;
	}
}
