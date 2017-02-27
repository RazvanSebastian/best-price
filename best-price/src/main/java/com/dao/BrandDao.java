package com.dao;

public class BrandDao {

	private String brandName;
	private String urlFirstPage;

	public BrandDao(String brandName, String urlFirstPage) {
		super();
		this.brandName = brandName;
		this.urlFirstPage = urlFirstPage;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getUrlFirstPage() {
		return urlFirstPage;
	}

	public void setUrlFirstPage(String urlFirstPage) {
		this.urlFirstPage = urlFirstPage;
	}

}
