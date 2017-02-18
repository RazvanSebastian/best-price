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
@Table(name = "user_laptop")
public class UserLaptop {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idUserLaptop;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "laptop_id")
	private Laptop laptop;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	public UserLaptop(Laptop laptop, User user) {
		super();
		this.laptop = laptop;
		this.user = user;
	}

	public int getIdUserLaptop() {
		return idUserLaptop;
	}

	public void setIdUserLaptop(int idUserLaptop) {
		this.idUserLaptop = idUserLaptop;
	}

	public Laptop getLaptop() {
		return laptop;
	}

	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
