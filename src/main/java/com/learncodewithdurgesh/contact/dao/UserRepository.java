package com.learncodewithdurgesh.contact.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learncodewithdurgesh.contact.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.email = :email")
	UserEntity getUserByUserName(@Param("email") String email);
	
//	Optional<UserEntity> findByEmail(String email);

}
