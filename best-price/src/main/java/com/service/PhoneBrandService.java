package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dao.BrandDao;
import com.jsoup.service.ProductInspectorService;
import com.model.PhoneBrand;
import com.repository.PhoneBrandRepository;

@Service
public class PhoneBrandService {

	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	@Autowired
	private PhoneBrandRepository phoneBrandRepository;

	@Transactional
	public void initializePhoneBrand() {
		PhoneBrand newBrand;
		List<PhoneBrand> phoneBrands = new ArrayList<>();
		for (BrandDao brand : this.emagInspectorService.getBrandsByProduct("phone")) {
			newBrand = new PhoneBrand();
			newBrand.setBrandName(brand.getBrandName());
			phoneBrands.add(newBrand);
		}
		this.phoneBrandRepository.save(phoneBrands);
	}
}
