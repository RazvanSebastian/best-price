package com.model;

import javax.persistence.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="user_phone")
public class UserPhone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idUserPhone;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phone_id")
	private Phone phone;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	

	public UserPhone(Phone phone, User user) {
		this.phone = phone;
		this.user = user;
	}

	public UserPhone() {
		// TODO Auto-generated constructor stub
	}

	public int getIdUserPhone() {
		return idUserPhone;
	}

	public void setIdUserPhone(int idUserPhone) {
		this.idUserPhone = idUserPhone;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
