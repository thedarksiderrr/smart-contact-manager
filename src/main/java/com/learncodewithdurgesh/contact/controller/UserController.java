package com.learncodewithdurgesh.contact.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.learncodewithdurgesh.contact.dao.ContactRepository;
import com.learncodewithdurgesh.contact.dao.UserRepository;
import com.learncodewithdurgesh.contact.entities.ContactEntity;
import com.learncodewithdurgesh.contact.entities.UserEntity;
import com.learncodewithdurgesh.contact.helper.Message;

@Controller
@RequestMapping("/user")

public class UserController {

	@Value("${file.upload-file-path}")
	private String uploadPath;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String userName = principal.getName();

		// get the user using username(Email)
		UserEntity user = userRepository.getUserByUserName(userName);

		model.addAttribute("user", user);
	}

	// dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "Dashboard");

		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new ContactEntity());

		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping(value = "/process-contact", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String processContact(@ModelAttribute ContactEntity contactEntity, Principal principal,
			@RequestParam(value = "image", required = false) MultipartFile file, HttpSession session) {

		try {
			String name = principal.getName();
			UserEntity user = userRepository.getUserByUserName(name);

			// give contact to user
			contactEntity.setUserEntity(user);

			// give user to contact
			user.getContactEntityList().add(contactEntity);

			// insert data into database
			UserEntity t = userRepository.save(user);

			int lastIndex = t.getContactEntityList().size() - 1;

			// uploading image
			String folderPath = uploadPath;
			File folder = new File(folderPath);
			folder.mkdirs();
			String filePath = folderPath + File.separator + t.getContactEntityList().get(lastIndex).getcId() + ".png";
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			// message of success
			session.setAttribute("message", new Message("Contact Added Successfully", "success"));
		} catch (IOException e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong !!", "danger"));
		}
		System.out.println("done" + contactEntity);

		return "normal/add_contact_form";
	}

	// get contact details
	// per page = 7[n]
	// current page = 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		model.addAttribute("title", "Show All Contacts");

		String userName = principal.getName();

		UserEntity user = userRepository.getUserByUserName(userName);

		// current page - page
		// contacts per page - 7
		PageRequest pageble = PageRequest.of(page, 7);

		Page<ContactEntity> contacts = contactRepository.findContactsByUser(user.getId(), pageble);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";

	}

	// getting users all details
	@GetMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Long cId, Model model, Principal principal) {

		Optional<ContactEntity> findById = contactRepository.findById(cId);
		ContactEntity contactEntity = findById.get();

		// checking valid signin user
		String userName = principal.getName();
		UserEntity userByUserName = userRepository.getUserByUserName(userName);

		if (userByUserName.getId() == contactEntity.getUserEntity().getId()) {
			model.addAttribute("contact", contactEntity);
			model.addAttribute("title", contactEntity.getName());
		}
		return "normal/contact_detail";
	}

	// delete user details
	@GetMapping("/deleteContact/{cId}")
	public String deleteContact(@PathVariable("cId") Long cId, Model model, HttpSession session, Principal principal) {

		ContactEntity contactEntity = contactRepository.findById(cId).get();

		// checking valid signin user
		String userName = principal.getName();
		UserEntity userByUserName = userRepository.getUserByUserName(userName);

		if (userByUserName.getId() == contactEntity.getUserEntity().getId()) {
			contactRepository.delete(contactEntity);

			contactEntity.setUserEntity(null);
			session.setAttribute("message", new Message("Something went wrong !!", "danger"));
		}
		session.setAttribute("message", new Message("Contact Delete Successfully !!", "success"));

		return "redirect:/user/show-contacts/0";
	}
	
	//update contact detail handler
	@PostMapping("/update-contact/{cId}")
	public String updateContactForm(@PathVariable("cId") Long cId, Model model) {

		model.addAttribute("title", "Update User Details");

		ContactEntity contactEntity = contactRepository.findById(cId).get();

		model.addAttribute("contact", contactEntity);

		return "normal/update_contact_form";
	}
	
	//processing for update contact details handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute ContactEntity contactEntity, Model model, HttpSession session,
			Principal principal) {

		try {
			/*----------------------------- Image ------------------------------*/
			// @RequestParam(value = "image", required = false) MultipartFile file,
			// , produces = MediaType.APPLICATION_JSON_VALUE, consumes =
			// MediaType.MULTIPART_FORM_DATA_VALUE
			// old contact details
			// ContactEntity oldContactDetail =
			// contactRepository.findById(contactEntity.getcId()).get();
			// image
			// if (!file.isEmpty()) {
			// int lastIndex = save.getContactEntityList().size() - 1;
			//
			// // uploading image
			// String folderPath = uploadPath;
			// File folder = new File(folderPath);
			// folder.mkdirs();
			// String filePath = folderPath + File.separator +
			// t.getContactEntityList().get(lastIndex).getcId() + ".png";
			// Files.copy(file.getInputStream(), Paths.get(filePath),
			// StandardCopyOption.REPLACE_EXISTING);
			//
			// }else {
			// contactEntity.setcId(contactEntity.getcId());
			// }
			/*----------------------------- Image ------------------------------*/

			UserEntity userEntity = userRepository.getUserByUserName(principal.getName());

			contactEntity.setUserEntity(userEntity);

			contactRepository.save(contactEntity);
			session.setAttribute("message", new Message("Contact Details Updated Successfully !!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong !!", "danger"));
		}

		return "redirect:/user/" + contactEntity.getcId() + "/contact";
	}
	
	//user profile page handler
	@GetMapping("/yourProfile")
	public String yourProfile(Model model) {

		model.addAttribute("title", "Your Profile");

		return "normal/profile";
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings(Model model) {

		model.addAttribute("title", "Settings - Smart Contact Manager");

		return "normal/settings";
	}
	
	//change password api
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {

		String userName = principal.getName();
		UserEntity userEntity = userRepository.getUserByUserName(userName);
		System.out.println(userEntity.getPassword());
		if (bCryptPasswordEncoder.matches(oldPassword, userEntity.getPassword())) {
			
			userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
			userRepository.save(userEntity);
			session.setAttribute("message", new Message("Password Changed Successfully !!", "success"));
			return "redirect:/user/index";
		} else {
			session.setAttribute("message", new Message("Please Enter Valid Password !!", "danger"));
			return "redirect:/user/settings";
		}
		
	}

}
