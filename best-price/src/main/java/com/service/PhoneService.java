package com.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jsoup.service.ProductInspectorService;
import com.repository.PhoneRepository;

@Service
public class PhoneService {
	
	@Autowired
	private PhoneRepository phoneRepostiroy;
	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	
	@Transactional
	public void initializePhoneTable(){
		this.phoneRepostiroy.save(this.emagInspectorService.getAllEmagPhones());
	}
}
