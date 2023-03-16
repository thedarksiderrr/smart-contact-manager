package com.learncodewithdurgesh.contact.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learncodewithdurgesh.contact.dao.UserRepository;
import com.learncodewithdurgesh.contact.entities.UserEntity;
import com.learncodewithdurgesh.contact.service.EmailServiceImpl;

@Controller
public class ForgotPasswordController {

	Random random = new Random(100000);

	@Autowired
	private EmailServiceImpl emailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// forgot password email form handler
	@GetMapping("/forgot")
	public String openEmailForm(Model model) {

		model.addAttribute("title", "Forgot Password");
		return "forgot_pass_email_form";
	}

	// generate otp handler
	@PostMapping("/send-otp")
	public String generateOTP(@RequestParam("email") String email, Model model, HttpSession session) {

		model.addAttribute("title", "Verify OTP");

		System.out.println(email);

		// generating otp
		int otp = random.nextInt(999999);
		System.out.println(otp);

		// write code for send otp to email
		String subject = "OTP From Smart Contact Manager";
		String message = "" + "<div style='border:1px solid #e2e2e2; padding:20px'>" + "<h1>" + "OTP is " + "<b>" + otp
				+ "</n>" + "</h1>" + "</div>";
		String to = email;

		boolean sendEmail = emailService.sendEmail(subject, message, to);

		if (sendEmail) {

			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);

			return "verify_otp";
		} else {
			session.setAttribute("message", "Please Check your email ID !!");

			return "forgot_pass_email_form";
		}
	}

	// verify otp and email id handler
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {

		int myOTP = (int) session.getAttribute("myotp");
		System.out.println("user otp " + otp);
		System.out.println("our otp " + myOTP);

		String email = (String) session.getAttribute("email");
		if (myOTP == otp) {

			// password change form
			UserEntity user = userRepository.getUserByUserName(email);
			if (user == null) {

				// send error message
				session.setAttribute("message", "User Doesn't exist with this email id !!");

				return "forgot_pass_email_form";

			} else {
				// session.setAttribute("message", "Change password Success");
				// send change password form

			}
			return "password_change_form";

		} else {

			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";
		}
	}

	// change-password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword, Model model, HttpSession session) {

		model.addAttribute("title", "Change Password Form");

		String email = (String) session.getAttribute("email");
		UserEntity userEntity = userRepository.getUserByUserName(email);
		if (newPassword.equals(confirmNewPassword)) {
			userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(userEntity);
			return "redirect:/signin?change=Password Changed Successfully";
		} else {
			session.setAttribute("message", "Password doesn't matched !!");
			return "password_change_form";
		}

	}

}
