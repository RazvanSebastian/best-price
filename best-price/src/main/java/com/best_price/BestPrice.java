package com.best_price;

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

import com.repository.LaptopRepository;
import com.repository.PhoneRepository;
import com.repository.RetailerRepository;
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
			
			@Override
			public void afterPropertiesSet() {
				if(this.retailerRepository.count() == 0)
					this.retailerService.initializeRetailers();
				if (this.phoneRepository.count() == 0) 
					this.inspectorService.initializePhoneTable();	
				if(this.laptopRepository.count() == 0)
					this.inspectorService.initializeLaptopTable();
			}
		};
	}
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(BestPrice.class, args);
    }
	
	
	
}
