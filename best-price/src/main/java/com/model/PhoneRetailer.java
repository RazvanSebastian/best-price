package com.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;

@Entity
@Table(name = "phone_retailer")
public class PhoneRetailer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idPhoneRetailer;

	// column association
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "phone_id")
	private Phone phone;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "retailer_id")
	private Retailer retailer;

	// extra column
	@Column(name = "phone_rating")
	private double rating;

	@Column(name = "phone_reviews")
	private int reviews;

	@Column(name = "phone_price")
	private double price;

	@Column(name = "retailer_offer_url")
	private String retailerOfferUrl;

	public String getRetailerOfferUrl() {
		return retailerOfferUrl;
	}

	public void setRetailerOfferUrl(String retailerOfferUrl) {
		this.retailerOfferUrl = retailerOfferUrl;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "currency")
	private MoneyCurrency moneyCurrency;

	@Enumerated(EnumType.STRING)
	@Column(name = "stock_state")
	private Stock stock;

	@Column(name = "last_date_price_check")
	private Date lastDateCheck;

	public PhoneRetailer() {

	}

	public PhoneRetailer(Phone phone, Retailer retailer, double price, Date lastDateCheck) {
		super();
		this.phone = phone;
		this.retailer = retailer;
		this.price = price;
		this.lastDateCheck = lastDateCheck;
	}

	public MoneyCurrency getMoneyCurrency() {
		return moneyCurrency;
	}

	public void setMoneyCurrency(MoneyCurrency moneyCurrency) {
		this.moneyCurrency = moneyCurrency;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public int getIdPhoneRetailer() {
		return idPhoneRetailer;
	}

	public void setIdPhoneRetailer(int idPhoneRetailer) {
		this.idPhoneRetailer = idPhoneRetailer;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Retailer getRetailer() {
		return retailer;
	}

	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getLastDateCheck() {
		return lastDateCheck;
	}

	public void setLastDateCheck(Date lastDateCheck) {
		this.lastDateCheck = lastDateCheck;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}
}
