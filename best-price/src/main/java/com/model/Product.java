package com.model;

import com.model.Product.MoneyCurrency;

public class Product {
	public enum MoneyCurrency {
		Lei, Euros, Dollars
	}

	public enum Stock {
		InStock, LimitedStock, OutOfStock, Soon
	}	
}
