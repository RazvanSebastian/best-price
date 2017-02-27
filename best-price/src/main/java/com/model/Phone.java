package com.model;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Phone")
public class Phone extends Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "phone_id")
	private Long idPhone;

	@Column(name = "phone_name")
	private String title;

	@Column(name = "phone_image")
	private String image;

	@OneToMany(mappedBy = "phone")
	private Set<PhoneRetailer> phoneRetailer = new HashSet<PhoneRetailer>();

	@OneToMany(mappedBy = "phone")
	private Set<UserPhone> userPhone = new HashSet<UserPhone>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phone_brand_id")
	private PhoneBrand phoneBrand;

	public Phone() {
	}

	public Set<UserPhone> getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(Set<UserPhone> userPhone) {
		this.userPhone = userPhone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getIdPhone() {
		return idPhone;
	}

	public void setIdPhone(Long idPhone) {
		this.idPhone = idPhone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;

	}

	public Set<PhoneRetailer> getPhoneRetailer() {
		return phoneRetailer;
	}

	public void setPhoneRetailer(Set<PhoneRetailer> phoneRetailer) {
		this.phoneRetailer = phoneRetailer;
	}

	public PhoneBrand getPhoneBrand() {
		return phoneBrand;
	}

	public void setPhoneBrand(PhoneBrand phoneBrand) {
		this.phoneBrand = phoneBrand;
	}

}
