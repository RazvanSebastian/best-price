package com.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Phone")
public class Phone extends Product{

	public enum MoneyCurrency {
		Lei, Euros, Dollars
	}

	public enum Stock {
		InStock, LimitedStock, OutOfStock, Soon
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idPhone;

	@Column(name = "phone_name")
	private String title;

	@Column(name = "phone_image")
	private String image;

	@Enumerated(EnumType.STRING)
	@Column(name = "stock_state")
	private Stock stock;

	@Column(name = "phone_price")
	private double price;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency")
	private MoneyCurrency moneyCurrency;

	@Column(name = "rating")
	private double rating;

	@Column(name = "phone_reviews")
	private int reviews;

	public Phone() {
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

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double d) {
		this.rating = d;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public MoneyCurrency getMoneyCurrency() {
		return moneyCurrency;
	}

	public void setMoneyCurrency(MoneyCurrency moneyCurrency) {
		this.moneyCurrency = moneyCurrency;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title + " PRICE=" + price + " " + moneyCurrency;

	}

}
