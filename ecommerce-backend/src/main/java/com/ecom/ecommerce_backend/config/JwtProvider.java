package com.ecom.ecommerce_backend.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	SecretKey key =Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
  
	public String generateToken(Authentication auth)
	{
		String jwt = Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 86000000)) // Token is validated for 24 hrs.
				.claim("email", auth.getName())
				.signWith(key).compact();
		
		return jwt;
	}
	
	public String extractEmailFromToken(String jwt)
	{
		jwt=jwt.substring(7); // To extract Bearere word.
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		
		// Extract Email from key
		String email = String.valueOf(claims.get("email"));
		return email;
	}
}
