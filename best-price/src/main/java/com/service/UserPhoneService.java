package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Phone;
import com.model.User;
import com.model.UserPhone;
import com.repository.PhoneRepository;
import com.repository.UserPhoneRepository;
import com.repository.UserRepository;

@Service
public class UserPhoneService {
	
	@Autowired
	private PhoneRepository phoneRepository;
	@Autowired
	private UserPhoneRepository userPhoneRepository;
	@Autowired 
	private UserRepository userRepository;
	
	@Transactional
	public void initUserPhoneTable(){
		List<Phone> phones=new ArrayList<>();
		phones.addAll(this.phoneRepository.findAll());
		User user = this.userRepository.findByEmail("rzvs95@gmail.com");
		UserPhone userPhone;
		for(Phone phone: phones){
			userPhone = new UserPhone();
			userPhone.setPhone(phone);
			userPhone.setUser(user);
			this.userPhoneRepository.save(userPhone);
		}
	}
}

