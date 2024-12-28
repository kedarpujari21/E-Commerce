package com.ecom.ecommerce_backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne // many product ratings belong to one user. 
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@ManyToOne // many product ratings belong to one product. 
	@JoinColumn(name="product_id", nullable = false)
	@JsonIgnore
	private Product product;
	
	@Column(name = "rating")
	private double rating;
	
	private LocalDateTime createdAt;
}
