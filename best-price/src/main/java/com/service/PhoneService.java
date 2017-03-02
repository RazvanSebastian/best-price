package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jsoup.service.ProductInspectorService;
import com.model.Phone;
import com.repository.PhoneRepository;

@Service
public class PhoneService {
	
	@Autowired
	private PhoneRepository phoneRepostiroy;
	
	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	
	@Autowired
	@Qualifier("altexInspector")
	private ProductInspectorService altexInspectorService;
	
	@Transactional
	public void initializePhoneTable(){
		List<Phone> phones=new ArrayList<Phone>();
		phones.addAll(this.emagInspectorService.getAllRetailerPhones());
		phones.addAll(this.altexInspectorService.getAllRetailerPhones());
		this.phoneRepostiroy.save(phones);
	}
}
