package com.jsoup.service;

import java.util.List;

import com.dao.BrandDao;
import com.model.Laptop;
import com.model.Phone;

public interface ProductInspectorService {
	public List<Phone> getAllEmagPhones();

	public List<Laptop> getAllEmagLaptops();

	public double inspectPhonePriceByUrl(String urlPhonePage);
	public List<BrandDao> getBrandsByProduct(String productType);
}
