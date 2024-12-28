package com.ecom.ecommerce_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.ecommerce_backend.config.JwtProvider;
import com.ecom.ecommerce_backend.exception.UserException;
import com.ecom.ecommerce_backend.model.User;
import com.ecom.ecommerce_backend.repository.UserRepository;
import com.ecom.ecommerce_backend.request.LoginRequest;
import com.ecom.ecommerce_backend.response.AuthResponse;
import com.ecom.ecommerce_backend.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserServiceImplementation customUserServiceImplementation;
	 
	//Registration Method
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user)throws UserException{
		
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		
		//Checking is user already exist with same email
		User isEmailExists = userRepository.findByEmail(email);
		
		if(isEmailExists != null) {
			throw new UserException("Email Already Exists");
		}
		
		// Creating new user..
		User createUser = new User();
		createUser.setEmail(email);
		createUser.setPassword(passwordEncoder.encode(password));// hashing the password..
		createUser.setFirstName(firstName);
		createUser.setLastName(lastName);
		
		//Saving newly created user into database
		User savedUser = userRepository.save(createUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignUp Successful..");
		
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}
	
	// Login method
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest){
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
        String token = jwtProvider.generateToken(authentication);
		
        AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignIn Successful..");
		
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
		
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
		
		if(userDetails == null)
		{
			throw new BadCredentialsException("Invalid Username..");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password..");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
	}
}
