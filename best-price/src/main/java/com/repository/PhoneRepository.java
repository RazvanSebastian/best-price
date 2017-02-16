package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone,Long>{
	
	
}
