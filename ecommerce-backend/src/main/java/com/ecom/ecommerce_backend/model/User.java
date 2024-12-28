package com.ecom.ecommerce_backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String password;
	
	private String email;
	
	private String role;
	
	private String mobileNo;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // CascadeType.ALL means , when the user deleted all the addresses related to that user also deleted.
	private List<Address> address = new ArrayList<>(); // one user can have many addresses. so OneToMany relationship
	
	@Embedded // this is not a separate entity
	@ElementCollection // To make separate table
	@CollectionTable(name="payment_information", joinColumns = @JoinColumn(name="user_id"))
	private List<PaymentInformation> paymentInformation = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // one user have many ratings..
	@JsonIgnore
	private List<Rating> ratings = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // one user have many reviews..
	@JsonIgnore
	private List<Review> reviews = new ArrayList<>();
	
	private LocalDateTime createdAt;
	
	public User() {
		
	}

	public User(Long id, String firstName, String lastName, String password, String email, String role, String mobileNo,
			List<Address> address, List<PaymentInformation> paymentInformation, List<Rating> ratings,
			List<Review> reviews, LocalDateTime createdAt) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.role = role;
		this.mobileNo = mobileNo;
		this.address = address;
		this.paymentInformation = paymentInformation;
		this.ratings = ratings;
		this.reviews = reviews;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<PaymentInformation> getPaymentInformation() {
		return paymentInformation;
	}

	public void setPaymentInformation(List<PaymentInformation> paymentInformation) {
		this.paymentInformation = paymentInformation;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
