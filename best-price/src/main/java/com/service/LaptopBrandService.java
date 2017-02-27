package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dao.BrandDao;
import com.jsoup.service.ProductInspectorService;
import com.model.LaptopBrand;
import com.repository.LaptopBrandRepository;

@Service
public class LaptopBrandService {

	@Autowired
	private LaptopBrandRepository laptopBrandRepository;

	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInsepctorService;
	
	@Transactional
	public void initializeLaptopBrand() {
		List<LaptopBrand> laptopBrands = new ArrayList<>();
		LaptopBrand newBrand;
		for (BrandDao brand : this.emagInsepctorService.getBrandsByProduct("laptop")) {
			if (brand.getBrandName().equals("HP")) {
				newBrand = new LaptopBrand();
				newBrand.setBrandName("hewlett_packard");
				laptopBrands.add(newBrand);
			} else {
				newBrand = new LaptopBrand();
				newBrand.setBrandName(brand.getBrandName());
				laptopBrands.add(newBrand);
			}
		}
		this.laptopBrandRepository.save(laptopBrands);
	}
}
