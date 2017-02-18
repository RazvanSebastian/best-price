package com.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.model.Phone;
import com.model.Product.MoneyCurrency;


@Service
public class EmailService{  
	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	private void sendMail(String from, String to, String subject, String msg) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		mailSender.send(message);
	}
	
	public void sendEmailPhonePriceChange(String userEmail,Phone phone,double newPrice,MoneyCurrency currency,String urlPhone){
		ApplicationContext context =
	             new ClassPathXmlApplicationContext("Spring-Mail.xml");

	    	EmailService mm = (EmailService) context.getBean("mailMail");
	        mm.sendMail("photo.book.api@gmail.com",
	    		   userEmail,
	    		   "New price!!!",
	    		   "There is a new price for the phone "+phone.getTitle()
	    		   +"/n/n The new price is "+newPrice+" "+currency 
	    		   +"/n/n If you need more details about product you can check next url "
	    		   +"/n/n "+urlPhone);

	    
	}
	
}  
