package com.learncodewithdurgesh.contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.learncodewithdurgesh.contact.configure.CustomUserDetails;
import com.learncodewithdurgesh.contact.dao.UserRepository;
import com.learncodewithdurgesh.contact.entities.UserEntity;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database

		UserEntity userByUserName = userRepository.getUserByUserName(username);

		if (userByUserName == null) {
			throw new UsernameNotFoundException("Could not found user!!");
		} 
		CustomUserDetails customUserDetails = new CustomUserDetails(userByUserName);

		return customUserDetails;
	}

}
