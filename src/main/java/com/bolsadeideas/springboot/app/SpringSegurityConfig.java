package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
public class SpringSegurityConfig extends WebSecurityConfigurerAdapter  {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
	PasswordEncoder encoder=PasswordEncoderFactories.createDelegatingPasswordEncoder();
	UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		//auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
		
	auth.inMemoryAuthentication()
	.withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
	.withUser(users.username("arnol").password("12345").roles("USER"));
	
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/","/css/**","/images/**","/js/**","/listar").permitAll()
		.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/factura/**").hasAnyRole("ADMIN")		
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login")
		.permitAll()
		.and()
		.logout().permitAll();
		/*.and()
		.formLogin().loginPage("/login").loginProcessingUrl("/logincheck")
		.usernameParameter("username").passwordParameter("password")
		.defaultSuccessUrl("/loginsuccess").permitAll()
		.and()
		.logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
		.permitAll();*/
		//super.configure(http);
	}
	
	
	
	
}
