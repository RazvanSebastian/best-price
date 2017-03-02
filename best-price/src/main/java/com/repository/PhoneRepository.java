package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Phone;
import com.model.PhoneBrand;

public interface PhoneRepository extends JpaRepository<Phone,Long>{
	
	@Query("SELECT phone FROM Phone phone WHERE phone.phoneBrand=:brand")
	List<Phone> findPhonesByBrand(@Param("brand") PhoneBrand brand);
	
	@Query("SELECT phone FROM Phone phone WHERE "
			+ "LOWER(phone.title) LIKE %:atr1% AND "
			+ "LOWER(phone.title) LIKE %:atr2% AND "
			+ "LOWER(phone.title) LIKE %:atr3% AND "
			+ "LOWER(phone.title) LIKE %:atrGb% AND "
			+ "LOWER(phone.title) LIKE %:lastAtr1% AND "
			+ "LOWER(phone.title) LIKE %:lastAtr2% AND "
			+ "LOWER(phone.title) LIKE %:lastAtr3%")
	List<Phone> findAllPhoneLikeAttributes(@Param("atr1") String atr1,@Param("atr2") String atr2,@Param("atr3") String atr3,@Param("atrGb") String atrGb,@Param("lastAtr1") String lastAtr1,@Param("lastAtr2") String lastAtr2,@Param("lastAtr3") String lastAtr3);
	
	
}
