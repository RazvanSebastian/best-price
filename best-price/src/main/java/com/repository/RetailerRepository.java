package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Retailer;

public interface RetailerRepository extends JpaRepository<Retailer,Long>{
	
	@Query("SELECT retailer FROM Retailer retailer WHERE retailer.name=:retailer")
	Retailer findRetailerByName(@Param("retailer") String retailerName);
	
}
