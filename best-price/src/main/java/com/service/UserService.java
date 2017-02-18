package com.service;

import java.util.*;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.exceptions.RegisterException;
import com.model.User;
import com.model.UserRole;
import com.repository.UserRepository;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	public final User loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		detailsChecker.check(user);
		return user;
	}
	
	private void checkUsernameAndEmail(User newUser) throws Exception{
		List<User> checkList=new ArrayList<>();
		checkList.addAll(this.userRepo.findUsersByUsernameOrEmail(newUser.getUsername(), newUser.getEmail()));
		if(checkList.size()!=0){
			int emailOk=1, usernameOk=1;
			for(User user:checkList){
				if(user.getEmail().equals(newUser.getEmail()));
					emailOk=0;
				if(user.getUsername().equals(newUser.getUsername()))
					usernameOk=0;
			}
			if(emailOk==0 && usernameOk==0)
				throw new RegisterException("Please choose another email and username!");
			if(emailOk==0)
				throw new RegisterException("Please choose another email!");
			if(usernameOk==0)
				throw new RegisterException("Please choose another user name!");
		}
	}
	
	public void registerNewUser(User newUser) throws Exception{
		
		if(newUser.getUsername().length()>=6){
			
			if(newUser.getPassword().length()<6)
				throw new RegisterException("Password must have at least 6 characters!");
			
			if(newUser.getEmail().length()!=0){
				this.checkUsernameAndEmail(newUser);
				if(EmailValidator.getInstance().isValid(newUser.getEmail())==false)
					throw new RegisterException("The email is invalid!");
			}
			else
				throw new RegisterException("Empty email input!");
		}
		else
			throw new RegisterException("User name must have at least 6 characters!");	
		
		if(newUser.getFirstName().length()==0)
			throw new RegisterException("First name input is empty!");
		if(newUser.getLastName().length()==0)
			throw new RegisterException("Last name input is empty!");
		newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
		newUser.grantRole(UserRole.USER);
		this.userRepo.save(newUser);
	}
}
