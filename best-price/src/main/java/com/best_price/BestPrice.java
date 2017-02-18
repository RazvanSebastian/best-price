package com.best_price;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.model.Phone;
import com.model.User;
import com.model.UserPhone;
import com.repository.LaptopRepository;
import com.repository.PhoneRepository;
import com.repository.RetailerRepository;
import com.repository.UserPhoneRepository;
import com.repository.UserRepository;
import com.service.ProductInspectorService;
import com.service.RetailerService;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages ={ "com.model"})
@ComponentScan(basePackages = { "com.service" , "com.security" , "com.restApi"})
@EnableJpaRepositories(basePackages={"com.repository"})
@EnableScheduling
public class BestPrice 
{
	
	@Bean
	@Transactional
	public InitializingBean insertProducts() {
		return new InitializingBean() {
			@Autowired
			@Qualifier("emagInspector")
			private ProductInspectorService inspectorService;
			@Autowired
			private RetailerService retailerService;
			
			@Autowired
			private PhoneRepository phoneRepository;
			@Autowired
			private LaptopRepository laptopRepository;
			@Autowired
			private RetailerRepository retailerRepository;
			@Autowired
			private UserPhoneRepository userPhoneRepository;
			@Autowired
			private UserRepository userRepository;
			
			@Override
			public void afterPropertiesSet() {
				if(this.retailerRepository.count() == 0)
					this.retailerService.initializeRetailers();
				if (this.phoneRepository.count() == 0) 
					this.inspectorService.initializePhoneTable();	
				if(this.laptopRepository.count() == 0)
					this.inspectorService.initializeLaptopTable();
				if(this.userPhoneRepository.count() == 0){
					List<Phone> phones=new ArrayList<>();
					phones.addAll(this.phoneRepository.findAll());
					User user = this.userRepository.findByEmail("rzvs95@gmail.com");
					UserPhone userPhone;
					for(Phone phone: phones){
						System.out.println(phone.getIdPhone());
						System.out.println(user.getEmail());
						userPhone = new UserPhone();
						userPhone.setPhone(phone);
						userPhone.setUser(user);
						userPhone.setIdUserPhone(1);
						this.userPhoneRepository.save(userPhone);
					System.out.println(phone.getTitle());
					}
				}
			}
		};
	}
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(BestPrice.class, args);
    }
	
	
	
}
