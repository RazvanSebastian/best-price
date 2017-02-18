package com.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.Phone;
import com.model.User;
import com.model.UserPhone;

@Transactional()
public interface UserPhoneRepository extends JpaRepository<UserPhone,Long>{
	
	@Query("SELECT DISTINCT userphone.phone FROM UserPhone userphone")
	List<Phone> selectDistinctPhones(); 
	
	@Query("SELECT userphone.user FROM UserPhone userphone WHERE userphone.phone=:phone")
	List<User> selectUsersByPhonesChecked(@Param("phone") Phone phone);

}
