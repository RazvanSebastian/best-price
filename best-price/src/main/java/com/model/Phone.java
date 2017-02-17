package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Phone")
public class Phone extends Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "phone_id")
	private int idPhone;

	@Column(name = "phone_name")
	private String title;

	@Column(name = "phone_image")
	private String image;

	@Column(name = "rating")
	private double rating;

	@Column(name = "phone_reviews")
	private int reviews;

	@OneToMany(mappedBy = "phone")
	private Set<PhoneRetailer> phoneRetailer = new HashSet<PhoneRetailer>();

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

	public int getIdPhone() {
		return idPhone;
	}

	public void setIdPhone(int idPhone) {
		this.idPhone = idPhone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;

	}

	public Set<PhoneRetailer> getPhoneRetailer() {
		return phoneRetailer;
	}

	public void setPhoneRetailer(Set<PhoneRetailer> phoneRetailer) {
		this.phoneRetailer = phoneRetailer;
	}

}
