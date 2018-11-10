package com.bolsadeideas.springboot.app.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bolsadeideas.springboot.app.models.entity.Usuario;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JWTAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;  
	
	public JWTAuthenticateFilter(AuthenticationManager authenticationManager) {		
		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login"));
	}



	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		
		 logger.info("Entro en attemptAuthentication");
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		
		

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();
		if(username != null && password != null){
			 logger.info("username desde request parameter (form - data): " +username);
			 logger.info("Password desde request parameter (form - data): " +password);
		}else {
			Usuario user = null;
			try {
				 user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				 username = user.getUsername();
				 password = user.getPassword();
				 
				 logger.info("username desde request parameter (form - data): " +username);
				 logger.info("Password desde request parameter (form - data): " +password);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		

		
		return authenticationManager.authenticate(authToken);
	}



	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String username = ((User) authResult.getPrincipal()).getUsername();
		Collection<? extends GrantedAuthority> roles=  authResult.getAuthorities();
		
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
		
		logger.info("antes del token"  );
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.signWith(SignatureAlgorithm.HS256,"Alguna.Clave.Secreta.Para.Arnol.es.123456".getBytes())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000*2))
				
				.compact();
		
		logger.info("valor de token" + token );
		response.addHeader("Authorization", "Bearer "+ token);
		
		 
		
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado session con éxito",username));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
		
		
		//super.successfulAuthentication(request, response, chain, authResult);
	}

	
	
}
