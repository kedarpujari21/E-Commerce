package com.ecom.ecommerce_backend.config;

import java.io.IOException;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 The JwtValidator class extends the OncePerRequestFilter, which is a convenient base class for Spring Security filters. 
 This class ensures that a filter is executed only once per request, regardless of the number of times the filter chain is invoked. 
 The doFilterInternal method is where you implement the logic for your custom filter, such as validating a JWT (JSON Web Token).
 */

public class JwtValidator extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader(JwtConstant.JWT_HEADER); // getting JWT_Header = "Authorization"
		if(jwt != null)
		{
			// Jwt token looks like Bearere jhjdhhdssd.
			//So to extract that Bearer word we start from 7 th index.
			jwt = jwt.substring(7);
			try {
				SecretKey key =Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
				
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				
				// Extract Email from key
				String email = String.valueOf(claims.get("email"));
				
				String authorities = String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email,null,auths); // username is email , pass null, authorities in auths.
					
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			} catch (Exception e) {
				throw new BadCredentialsException("Invalid Token");
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
