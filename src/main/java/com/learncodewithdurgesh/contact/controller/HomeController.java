package com.learncodewithdurgesh.contact.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learncodewithdurgesh.contact.dao.UserRepository;
import com.learncodewithdurgesh.contact.entities.UserEntity;
import com.learncodewithdurgesh.contact.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	// handler for home/main page
	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home - Smart Contact Manager");

		return "home";
	}

	// handler for about page
	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About - Smart Contact Manager");

		return "about";
	}

	// handler for signup page
	@GetMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new UserEntity());
		return "signup";
	}

	// handler for registration user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") UserEntity user,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			BindingResult result, HttpSession session) {

		try {
			if (!agreement) {
				throw new Exception("Please accept terms and conditions");
			}
			if (result.hasErrors()) {
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageURL("programmer.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			// console only
			System.out.println("Agreement " + agreement);
			System.out.println("User " + user);
			// console only

			// insert data into database
			UserEntity saveUserEntity = userRepository.save(user);
			model.addAttribute("user", new UserEntity());
			session.setAttribute("message", new Message("Successfully Registered !! ", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));

			return "signup";
		}

	}

	// handler for custom login (user)
	@GetMapping("/signin")
	public String customLogin(Model model) {

		model.addAttribute("title", "Login - Smart Contact Manager");

		return "login";
	}
}
