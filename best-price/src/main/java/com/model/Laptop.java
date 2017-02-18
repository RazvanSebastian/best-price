package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Laptop")
public class Laptop extends Product{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "laptop_id")
	private int idLaptop;

	@Column(name = "laptop_name")
	private String title;

	@Column(name = "laptop_image")
	private String image;

	@Column(name = "laptop_rating")
	private double rating;

	@Column(name = "laptop_reviews")
	private int reviews;
	
	@OneToMany(mappedBy = "laptop")
	private Set<LaptopRetailer> laptopRetailer = new HashSet<LaptopRetailer>();

	@OneToMany(mappedBy = "laptop")
	private Set<UserLaptop> userLaptop = new HashSet<UserLaptop>();

	
	public Laptop() {
	}

	public Laptop(String title, String image, double rating, int reviews) {
		this.title = title;
		this.image = image;
		this.rating = rating;
		this.reviews = reviews;
	}

	public int getIdLaptop() {
		return idLaptop;
	}

	public void setIdLaptop(int idLaptop) {
		this.idLaptop = idLaptop;
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

	public Set<LaptopRetailer> getLaptopRetailer() {
		return laptopRetailer;
	}

	public void setLaptopRetailer(Set<LaptopRetailer> laptopRetailer) {
		this.laptopRetailer = laptopRetailer;
	}

	public Set<UserLaptop> getUserLaptop() {
		return userLaptop;
	}

	public void setUserLaptop(Set<UserLaptop> userLaptop) {
		this.userLaptop = userLaptop;
	}
	
	
	
}
