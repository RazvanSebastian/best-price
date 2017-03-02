package com.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Retailer;
import com.repository.RetailerRepository;

@Service
public class RetailerService {
	
	@Autowired
	private RetailerRepository retailerRepository;
	
	public void initializeRetailers(){
		List<Retailer> retailers = new ArrayList<Retailer>();
		retailers.add(new Retailer("Emag","http://www.emag.ro","https://pbs.twimg.com/profile_images/686470908001796096/rbxi0wBr.png"));
		retailers.add(new Retailer("Altex","http://www.altex.ro","http://cetin.ro/poze/2016/07/altex-logo.jpg"));
		this.retailerRepository.save(retailers);
	}
	
}
