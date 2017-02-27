package com.model;

import com.model.Product.MoneyCurrency;

public class Product {
	public enum MoneyCurrency {
		Lei, Euros, Dollars
	}

	public enum Stock {
		InStock, LimitedStock, OutOfStock, Soon
	}
	
	public MoneyCurrency convertToMoneyCurrency(String currency){
		if (currency.equals("Lei"))
			return MoneyCurrency.Lei;
		if (currency.equals("Euros"))
			return MoneyCurrency.Euros;
		if (currency.equals("Dollars"))
			return MoneyCurrency.Dollars;
		return MoneyCurrency.Euros;
	}
	
}
