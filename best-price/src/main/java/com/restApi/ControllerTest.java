package com.restApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {
	
	@RequestMapping(value="")
	public String getHelloWorld(){
		return "Hello World!";
	}
	
	@RequestMapping(value="/admin/get")
	public String adminRights(){
		return "";
	}
	
}
