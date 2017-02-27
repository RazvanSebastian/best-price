package com.service;

import java.util.ArrayList;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jsoup.service.ProductInspectorService;
import com.model.Phone;
import com.model.PhoneRetailer;
import com.model.Retailer;
import com.model.User;
import com.model.Product.MoneyCurrency;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;
import com.repository.UserPhoneRepository;

@Service
public class ScheduleCheckerService {

	@Autowired
	private UserPhoneRepository userPhoneRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;
	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	@Autowired
	private EmailService emailService;

	
	@Transactional
	private void checkAllUserPhonesPrices() {
		List<PhoneRetailer> phoneRetailer=this.phoneRetailerRepository.findAllPhonesRetailerByPhoneList(this.userPhoneRepository.selectDistinctPhones());
		for(PhoneRetailer pr:phoneRetailer){
			if(pr.getRetailer().getRetailerName().equals("Emag")){
				Double newPrice=emagInspectorService.inspectPhonePriceByUrl(pr.getRetailerOfferUrl());
				if(newPrice<pr.getPrice()){
					this.phoneRetailerRepository.updatePriceByPhoneAndRetailer(newPrice, pr.getPhone(), pr.getRetailer());
					List<User> users = this.userPhoneRepository.selectUsersByPhonesChecked(pr.getPhone());
					this.sendEmailToUsers(users, pr.getPhone(),pr.getRetailer(), newPrice, pr.getMoneyCurrency(),pr.getRetailerOfferUrl());
				}
			}
			if(pr.getRetailer().getRetailerName().equals("Altex")){
				//same actions for Altex retailer
			}
		}
	}
	
	private void sendEmailToUsers(List<User> users,Phone phone,Retailer retailer,double newPrice,MoneyCurrency currency,String urlPhone){
		for(User user:users){
			this.emailService.sendEmailPhonePriceChange(user.getEmail(),phone,retailer, newPrice, currency, urlPhone);
		}
	}
	
	/**
	 * Documentation :
	 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
	 */
	@Scheduled(cron="*/30 * * * * *")
	private void checkEmagPrices(){
		this.checkAllUserPhonesPrices();
	}
			
	
}
