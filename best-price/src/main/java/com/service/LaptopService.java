package com.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jsoup.service.ProductInspectorService;
import com.repository.LaptopRepository;

@Service
public class LaptopService {
	
	@Autowired
	private LaptopRepository laptopRepostiroy;
	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	
	@Transactional
	public void initializeLaptopTable(){
		this.laptopRepostiroy.save(this.emagInspectorService.getAllEmagLaptops());
	}
	
}
