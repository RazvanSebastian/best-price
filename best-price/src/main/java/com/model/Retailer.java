package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="retatiler")
public class Retailer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idRetailer;
	
	@NotNull
	@Column(name="retailer_name")
	private String retailerName;
	
	@NotNull
	@Column(name="retailer_url")
	private String retailerUrl;
	
	@NotNull
	@Column(name="retailer_emblem_url")
	private String retailerEmblem;

	public Retailer() {
		super();
	}

	public Retailer(String retailerName, String retailerUrl, String retailerEmblem) {
		this.retailerName = retailerName;
		this.retailerUrl = retailerUrl;
		this.retailerEmblem = retailerEmblem;
	}

	public int getIdRetailer() {
		return idRetailer;
	}

	public void setIdRetailer(int idRetailer) {
		this.idRetailer = idRetailer;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getRetailerUrl() {
		return retailerUrl;
	}

	public void setRetailerUrl(String retailerUrl) {
		this.retailerUrl = retailerUrl;
	}

	public String getRetailerEmblem() {
		return retailerEmblem;
	}

	public void setRetailerEmblem(String retailerEmblem) {
		this.retailerEmblem = retailerEmblem;
	}
}
