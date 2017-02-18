package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="retatiler")
public class Retailer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="retailer_id")
	private int idRetailer;
	
	@NotNull
	@Column(name="retailer_name")
	private String name;
	
	@NotNull
	@Column(name="retailer_url")
	private String siteUrl;
	
	@NotNull
	@Column(name="retailer_emblem_url")
	private String emblem;
	
	@OneToMany(mappedBy = "retailer")
	private Set<PhoneRetailer> phoneRetailer = new HashSet<PhoneRetailer>();
	
	@OneToMany(mappedBy = "retailer" )
	private Set<LaptopRetailer> laptopRetailer = new HashSet<LaptopRetailer>();

	public Retailer() {
		super();
	}

	public Retailer(String retailerName, String retailerUrl, String retailerEmblem) {
		this.name = retailerName;
		this.siteUrl = retailerUrl;
		this.emblem = retailerEmblem;
	}

	public int getIdRetailer() {
		return idRetailer;
	}

	public void setIdRetailer(int idRetailer) {
		this.idRetailer = idRetailer;
	}

	public String getRetailerName() {
		return name;
	}

	public void setRetailerName(String retailerName) {
		this.name = retailerName;
	}

	public String getRetailerUrl() {
		return siteUrl;
	}

	public void setRetailerUrl(String retailerUrl) {
		this.siteUrl = retailerUrl;
	}

	public String getRetailerEmblem() {
		return emblem;
	}

	public void setRetailerEmblem(String retailerEmblem) {
		this.emblem = retailerEmblem;
	}
	
	
	public Set<PhoneRetailer> getPhoneRetailer() {
		return phoneRetailer;
	}

	public void setPhoneRetailer(Set<PhoneRetailer> phoneRetailer) {
		this.phoneRetailer = phoneRetailer;
	}

	public Set<LaptopRetailer> getLaptopRetailer() {
		return laptopRetailer;
	}

	public void setLaptopRetailer(Set<LaptopRetailer> laptopRetailer) {
		this.laptopRetailer = laptopRetailer;
	}
}
