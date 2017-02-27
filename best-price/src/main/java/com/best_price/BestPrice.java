package com.best_price;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.repository.LaptopBrandRepository;
import com.repository.LaptopRepository;
import com.repository.PhoneBrandRepository;
import com.repository.PhoneRepository;
import com.repository.RetailerRepository;
import com.repository.UserPhoneRepository;
import com.service.LaptopBrandService;
import com.service.LaptopService;
import com.service.PhoneBrandService;
import com.service.PhoneService;
import com.service.RetailerService;
import com.service.UserPhoneService;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = { "com.model" })
@ComponentScan(basePackages = { "com.service", "com.security", "com.restApi", "com.jsoup.service" })
@EnableJpaRepositories(basePackages = { "com.repository" })
@EnableScheduling
public class BestPrice {

	@Bean
	public InitializingBean initTables() {
		return new InitializingBean() {
			@Autowired
			private PhoneService phoneService;
			@Autowired
			private LaptopService laptopService;
			@Autowired
			private RetailerService retailerService;
			@Autowired
			private UserPhoneService userPhoneService;
			@Autowired
			private PhoneBrandService phoneBrandService;	
			@Autowired 
			private LaptopBrandService laptopBrandService;

			@Autowired
			private LaptopBrandRepository laptopBrandRepository;
			@Autowired
			private PhoneRepository phoneRepository;
			@Autowired
			private LaptopRepository laptopRepository;
			@Autowired
			private RetailerRepository retailerRepository;
			@Autowired
			private UserPhoneRepository userPhoneRepository;
			@Autowired
			private PhoneBrandRepository phoneBrandRepository;
			
			@Override
			public void afterPropertiesSet() {
				//init brands
				if(this.laptopBrandRepository.count()==0)
					this.laptopBrandService.initializeLaptopBrand();
				if(this.phoneBrandRepository.count() == 0)
					this.phoneBrandService.initializePhoneBrand();
				//init retailer and products
				if (this.retailerRepository.count() == 0)
					this.retailerService.initializeRetailers();
				if (this.laptopRepository.count() == 0)
					this.laptopService.initializeLaptopTable();
				if (this.phoneRepository.count() == 0)
					this.phoneService.initializePhoneTable();
				
				
				if (this.userPhoneRepository.count() == 0)
					this.userPhoneService.initUserPhoneTable();

			}
		};
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BestPrice.class, args);
	}

}
