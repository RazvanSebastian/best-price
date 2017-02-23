package com.model;

import java.util.Date;

import javax.persistence.*;

import com.model.Product.MoneyCurrency;
import com.model.Product.Stock;

@Entity
@Table(name = "laptop_retailer")
public class LaptopRetailer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idPhoneRetailer;

	// column association
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "laptop_id")
	private Laptop laptop;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "retailer_id")
	private Retailer retailer;

	// extra column
	@Column(name = "retailer_offer_url")
	private String retailerOfferUrl;

	@Column(name = "laptop_price")
	private double price;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency")
	private MoneyCurrency moneyCurrency;

	@Enumerated(EnumType.STRING)
	@Column(name = "stock_state")
	private Stock stock;

	@Column(name = "last_date_price_check")
	private Date lastDateCheck;

	public LaptopRetailer(Laptop laptop, Retailer retailer, double price, MoneyCurrency moneyCurrency, Stock stock,
			Date lastDateCheck) {
		super();
		this.laptop = laptop;
		this.retailer = retailer;
		this.price = price;
		this.moneyCurrency = moneyCurrency;
		this.stock = stock;
		this.lastDateCheck = lastDateCheck;
	}

	public LaptopRetailer() {
		// TODO Auto-generated constructor stub
	}

	public int getIdPhoneRetailer() {
		return idPhoneRetailer;
	}

	public void setIdPhoneRetailer(int idPhoneRetailer) {
		this.idPhoneRetailer = idPhoneRetailer;
	}

	public Laptop getLaptop() {
		return laptop;
	}

	public void setLaptop(Laptop laptop) {
		this.laptop = laptop;
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

	public Date getLastDateCheck() {
		return lastDateCheck;
	}

	public void setLastDateCheck(Date lastDateCheck) {
		this.lastDateCheck = lastDateCheck;
	}

	public String getRetailerOfferUrl() {
		return retailerOfferUrl;
	}

	public void setRetailerOfferUrl(String retailerOfferUrl) {
		this.retailerOfferUrl = retailerOfferUrl;
	}

}
