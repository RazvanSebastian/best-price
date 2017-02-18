package com.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.User;
import com.security.TokenHandler;
import com.security.UserAuthentication;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "Cookies";
	 private static final String USER_DETAILS = "User-Details";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

	private final TokenHandler tokenHandler;

	@Autowired
	public TokenAuthenticationService(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}
	
	//after login I will receivce in response header the token as name of USER_DETAILS
	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) throws JsonProcessingException {
		//using User Authentication receive details about user
		final User user = authentication.getDetails();
		//as name of X-AUTH-TOKEN I create one field in header with the token code
		response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
		 ObjectMapper mapper = new ObjectMapper();
		 //as name of USER-DETAILS what am I mapping on front-end i get string map of user details
		 response.addHeader(USER_DETAILS, mapper.writeValueAsString(user));
		 
	}
	
	//get request from front-end server
	public Authentication getAuthentication(HttpServletRequest request) {
		//extract from header X-AUTH-TOKEN
		final String token = request.getHeader(AUTH_HEADER_NAME);

		if (token != null) {
			//i parsing the token to user object
			final User user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(user);
			}
		}
		 return null;
		
	}
}