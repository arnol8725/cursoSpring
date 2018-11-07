package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSeccessHandler;
import com.bolsadeideas.springboot.app.models.service.JpaUserDetailServices;;





@EnableGlobalMethodSecurity(securedEnabled=true,prePostEnabled=true)
@Configuration
public class SpringSegurityConfig extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	private LoginSeccessHandler successHandler;
	
	@Autowired 
	private DataSource dataSourse;
	
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaUserDetailServices userDetailsServices;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		/*auth.jdbcAuthentication()
		.dataSource(dataSourse)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username,password, enabled  from users where username=?")
		.authoritiesByUsernameQuery("select u.username,a.authority from authorites a inner join users u on (a.user_id=u.id) where  u.username=?");*/
		
		auth.userDetailsService(userDetailsServices).passwordEncoder(passwordEncoder);
	/*PasswordEncoder encoder=PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		//auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
		
	auth.inMemoryAuthentication()
	.withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
	.withUser(users.username("arnol").password("12345").roles("USER"));*/
	
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/","/css/**","/images/**","/js/**","/listar").permitAll()
		/*.antMatchers("/ver/**").hasAnyRole("USER")*/
		/*.antMatchers("/uploads/**").hasAnyRole("USER")*/
		/*.antMatchers("/form/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		/*.antMatchers("/factura/**").hasAnyRole("ADMIN")	*/	
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.successHandler(successHandler)
		.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403")
		;
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
