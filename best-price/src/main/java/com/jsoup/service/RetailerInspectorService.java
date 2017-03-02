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
import org.springframework.stereotype.Service;

import com.dao.BrandDao;
import com.model.Laptop;
import com.model.LaptopBrand;
import com.model.LaptopRetailer;
import com.model.Phone;
import com.model.PhoneBrand;
import com.model.PhoneRetailer;
import com.model.Product;
import com.model.Retailer;
import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;

@Service
public abstract class RetailerInspectorService implements ProductInspectorService {

	protected abstract Document receivePageByUrl(String url);

	public abstract int getnumberOfPages(String productTypePage);

	/**
	 * Get product details
	 */
	public abstract String getProductImage(Element productHolder);

	public abstract String getProductTitle(Element productHolder, String productType);

	public abstract Double getProductRating(Element productHolder);

	public abstract int getProductReview(Element productHolder);

	public abstract String getRetailerUrlOfProductOffer(Element productHolder);

	public abstract Stock getProductStokState(Element productHolder);
	
	public abstract MoneyCurrency getProductCurrency(Element productHolder);

	public abstract String getProductPrice(Element productHolder);

	/**
	 * Methods used for inner by many-many tables
	 */

	protected abstract Phone setNewPhoneAttributes(Element productHolder);

	protected abstract PhoneRetailer setNewPhoneRetailerAttributes(Element productHolder, Phone newPhone,
			Retailer retailer);

	protected abstract Laptop setNewLaptopAttributes(Element productHolder);

	protected abstract LaptopRetailer setNewLaptopRetailerAttributes(Element productHolder, Laptop newLaptop,
			Retailer retailer);

	protected abstract List<Product> getAllProductFromPage(Document doc, String productType);

	/**
	 * Methods used in other packages (Interface methods)
	 */
	public abstract List<BrandDao> getBrandsByProduct(String productType);

	public abstract List<Phone> getAllRetailerPhones();

	public abstract List<Laptop> getAllRetailerLaptops();

	public abstract double inspectPhonePriceByUrl(String urlPhonePage);

}
