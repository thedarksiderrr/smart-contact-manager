package com.learncodewithdurgesh.contact.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	@NotBlank(message = "Name Field is Required!")
	@Size(min = 2, max = 20, message = "Min. 2 or Max. 20 Characters are allowed")
	@Column
	private String name;

	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
	@Column(unique = true)
	private String email;
	@Column
	private String password;
	@Column
	private String role;
	@Column
	private String imageURL;
	@Column(length = 500)
	private String about;
	@Column
	private boolean enabled;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userEntity")
	private List<ContactEntity> contactEntityList = new ArrayList<>();

	public UserEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<ContactEntity> getContactEntityList() {
		return contactEntityList;
	}

	public void setContactEntityList(List<ContactEntity> contactEntityList) {
		this.contactEntityList = contactEntityList;
	}

//	@Override
//	public String toString() {
//		return "UserEntity [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
//				+ role + ", imageURL=" + imageURL + ", about=" + about + ", enabled=" + enabled + ", contactEntityList="
//				+ contactEntityList + "]";
//	}

}
