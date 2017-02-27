package com.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="laptop_brand")
public class LaptopBrand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "laptop_brand_id")
	private int idLaptopeBrand;

	@Column(name = "brand_name")
	private String brandName;
	
	@OneToMany(mappedBy = "laptopBrand")
	private Set<Laptop> laptops= new HashSet<Laptop>();

	public int getIdLaptopeBrand() {
		return idLaptopeBrand;
	}

	public void setIdLaptopeBrand(int idLaptopeBrand) {
		this.idLaptopeBrand = idLaptopeBrand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Set<Laptop> getLaptops() {
		return laptops;
	}

	public void setLaptops(Set<Laptop> laptops) {
		this.laptops = laptops;
	}

}
