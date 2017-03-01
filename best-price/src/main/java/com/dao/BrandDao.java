package com.dao;

public class BrandDao {

	private String brandName;
	private String urlFirstPage;
	private int numberOfPages;

	
	
	public BrandDao(String brandName, String urlFirstPage, int numberOfPages) {
		super();
		this.brandName = brandName;
		this.urlFirstPage = urlFirstPage;
		this.numberOfPages = numberOfPages;
	}

	public BrandDao(String brandName, String urlFirstPage) {
		super();
		this.brandName = brandName;
		this.urlFirstPage = urlFirstPage;
	}

	public BrandDao() {
		// TODO Auto-generated constructor stub
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

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}



}
