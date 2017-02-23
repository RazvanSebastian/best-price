package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.model.Phone;
import com.model.PhoneRetailer;
import com.model.Retailer;

@Transactional()
public interface PhoneRetailerRepository extends JpaRepository<PhoneRetailer, Long> {

	@Query("SELECT phone_retailer FROM PhoneRetailer phone_retailer WHERE phone_retailer.phone IN :phone_list")
	List<PhoneRetailer> findAllPhonesRetailerByPhoneList(@Param("phone_list") List<Phone> phoneList);
	
	@Transactional
	@Modifying
	@Query("UPDATE PhoneRetailer phone_retailer SET phone_retailer.price=:new_price WHERE phone_retailer.phone=:phone AND phone_retailer.retailer=:retailer")
	void updatePriceByPhoneAndRetailer(@Param("new_price") double price,@Param("phone") Phone phone, @Param("retailer") Retailer retailer);
}
