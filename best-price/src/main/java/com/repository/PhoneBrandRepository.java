package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.PhoneBrand;

public interface PhoneBrandRepository extends JpaRepository<PhoneBrand,Long>{
	
	@Query("SELECT brand FROM PhoneBrand brand WHERE LOWER(brand.brandName)=LOWER(:brandName)")
	PhoneBrand findByBrandName(@Param("brandName") String brandName);
}
