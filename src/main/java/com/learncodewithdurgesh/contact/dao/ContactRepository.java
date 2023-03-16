package com.learncodewithdurgesh.contact.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learncodewithdurgesh.contact.entities.ContactEntity;
import com.learncodewithdurgesh.contact.entities.UserEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

	@Query("from ContactEntity as c where c.userEntity.id = :userId")
	// current page - page
	// contacts per page - 7
	public Page<ContactEntity> findContactsByUser(@Param("userId") Long userId, Pageable pageable);

//	@Modifying
//	@Transactional
//	@Query("delete from ContactEntity c where c.userEntity.id =:id")
//	public void deleteContactById(@Param("id") Integer id);

	// search
	public List<ContactEntity> findByNameIgnoreCaseContainingAndUserEntity(String name, UserEntity userEntity);
}
