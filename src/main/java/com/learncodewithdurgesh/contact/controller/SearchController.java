package com.learncodewithdurgesh.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.learncodewithdurgesh.contact.dao.ContactRepository;
import com.learncodewithdurgesh.contact.dao.UserRepository;
import com.learncodewithdurgesh.contact.entities.ContactEntity;
import com.learncodewithdurgesh.contact.entities.UserEntity;

@RestController
public class SearchController {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {

		UserEntity userEntity = userRepository.getUserByUserName(principal.getName());
		List<ContactEntity> contactEntities = contactRepository.findByNameIgnoreCaseContainingAndUserEntity(query,
				userEntity);
		return ResponseEntity.ok(contactEntities);

	}

}
