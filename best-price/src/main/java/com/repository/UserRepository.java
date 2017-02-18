package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	
	@Query("SELECT user FROM User user WHERE user.username=:username OR user.email=:email")
	List<User>	findUsersByUsernameOrEmail(@Param("username") String username,@Param("email") String email);
	
	@Query("SELECT user FROM User user WHERE user.email=:email")
	User findByEmail(@Param("email") String email);

}
