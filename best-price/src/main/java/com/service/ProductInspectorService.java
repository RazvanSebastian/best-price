package com.service;


public interface ProductInspectorService{
	public void initializePhoneTable();
	public void initializeLaptopTable();
	public double inspectPhonePriceByUrl(String urlPhonePage);
}
