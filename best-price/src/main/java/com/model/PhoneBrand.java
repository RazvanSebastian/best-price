package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "phone_brand")
public class PhoneBrand {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "phone_brand_id")
	private int idPhoneBrand;

	@Column(name = "brand_name")
	private String brandName;
	
	@OneToMany(mappedBy = "phoneBrand")
	private Set<Phone> phones = new HashSet<Phone>();

	public int getIdPhoneBrand() {
		return idPhoneBrand;
	}

	public void setIdPhoneBrand(int idPhoneBrand) {
		this.idPhoneBrand = idPhoneBrand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

}
