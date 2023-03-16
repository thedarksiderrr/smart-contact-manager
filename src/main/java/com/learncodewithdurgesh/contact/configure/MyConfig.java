package com.learncodewithdurgesh.contact.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.learncodewithdurgesh.contact.service.UserDetailsServiceImpl;

@Configuration
public class MyConfig {
	
//	@Bean
//	public SearchController controller() {
//		
//		return new SearchController();
//	}
	
	@Bean
	public UserDetailsService getUserDetailService() {

		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

	// configure method

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {

		return auth.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().authorizeHttpRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**")
				.hasRole("USER").antMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
//				.loginProcessingUrl("/doUserlogin")
				.defaultSuccessUrl("/user/index")
//				.failureUrl("/login-fail")
				.and().csrf().disable();

//		http.formLogin().loginProcessingUrl("/dologin");
//		http.formLogin().defaultSuccessUrl("/user/index", true);

		return http.build();
	}

}
