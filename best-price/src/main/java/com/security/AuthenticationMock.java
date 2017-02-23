package com.security;

import org.springframework.beans.factory.InitializingBean;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;

import com.model.User;
import com.model.UserRole;
import com.repository.UserRepository;


@EnableAutoConfiguration
@Configuration
@ComponentScan
public class AuthenticationMock {

	@Bean
	public InitializingBean insertDefaultUsers() {
		return new InitializingBean() {
			@Autowired
			private UserRepository userRepository;

			@Override
			public void afterPropertiesSet() {
				if (this.userRepository.count() == 0) {
					addUser("admin", "admin");
					addUser("user", "user");
				}
			}

			private void addUser(String username, String password) {
				User user = new User();
				user.setUsername(username);
			
				user.setPassword(new BCryptPasswordEncoder().encode(password));
				if(username.equals("admin")){
					user.setEmail("rzvs95@gmail.com");
					user.grantRole(UserRole.ADMIN);
					user.grantRole(UserRole.USER);
					user.setFirstName("Razvan");
					user.setLastName("Parautiu");
				}
				else{
					user.setEmail("user");
					user.grantRole(UserRole.USER);
					user.setFirstName("Alex");
					user.setLastName("Popescu");
				}
				userRepository.save(user);
			}
		};
	}

	@Bean
	public Filter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}
}
