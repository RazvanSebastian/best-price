package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.model.Phone;
import com.model.PhoneRetailer;
import com.model.Retailer;
import com.model.User;
import com.model.UserPhone;
import com.model.Product.MoneyCurrency;
import com.repository.PhoneRetailerRepository;
import com.repository.RetailerRepository;
import com.repository.UserPhoneRepository;
import com.repository.UserRepository;

@Service
public class ScheduleCheckerService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserPhoneRepository userPhoneRepository;
	@Autowired
	private PhoneRetailerRepository phoneRetailerRepository;
	@Autowired
	@Qualifier("emagInspector")
	private ProductInspectorService emagInspectorService;
	@Autowired
	private RetailerRepository retailerRepository;
	@Autowired
	private EmailService emailService;
	/**
	 * Documentation :
	 * http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
	 */
	@Transactional
	@Scheduled(cron = "*/20 * * * * *")
	public void checkAllUserPhonePrices() {
		Retailer retailer=this.retailerRepository.findRetailerByName("Emag");
		//get all phones checked by users
		List<Phone> phoneList = new ArrayList<Phone>();
		phoneList.addAll(this.userPhoneRepository.selectDistinctPhones());
		//get all offers from retailers to get url product to check if there is a new price
		List<PhoneRetailer> prList=new ArrayList<>();
		prList.addAll(this.phoneRetailerRepository.findAllPhonesRetailerByList(retailer,phoneList));
		
		for(PhoneRetailer pr:prList){
			Double newPrice=emagInspectorService.inspectPhonePriceByUrl(pr.getRetailerOfferUrl());	
			if(newPrice!=pr.getPrice()){
				//action
				//System.out.println(pr.getPhone().getTitle()+" PRICE CHANGED!!!");
				this.phoneRetailerRepository.updatePriceByPhoneAndRetailer(newPrice,pr.getPhone(), retailer);
				List<User> users = this.userPhoneRepository.selectUsersByPhonesChecked(pr.getPhone());
				this.sendEmailToUsers(users, pr.getPhone(), newPrice, pr.getMoneyCurrency(),pr.getRetailerOfferUrl());
			}
		}
		System.out.println("Checked!");
	}
	
	private void sendEmailToUsers(List<User> users,Phone phone,double newPrice,MoneyCurrency currency,String urlPhone){
		for(User user:users){
			this.emailService.sendEmailPhonePriceChange(user.getEmail(),phone, newPrice, currency, urlPhone);
		}
	}
			
	
}
