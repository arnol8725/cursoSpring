package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.Usuario;


@Service("jpaUserDetailServices")
public class JpaUserDetailServices implements UserDetailsService {
	private static final Log LOG = LogFactory.getLog(JpaUserDetailServices.class);
	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		LOG.info("loadUserByUsername" + username);
		Usuario user = usuarioDao.findByUsername(username);
		LOG.info("paso " + username);    
		if (user == null) {
			LOG.info("Error  login usuario" +username+ " no tiene usuario asignados");
			throw new UsernameNotFoundException("username " + username +" no tiene roles asignados" );
		}
		
		LOG.info("user" + user.toString());
		
		List<GrantedAuthority> autorities = buildAuthorities(user.getRoles());
		LOG.info("aut"+autorities);
		if (autorities.isEmpty()) {
			LOG.info("Error  login usuario" +username+ " no tiene roles asignados");
			throw new UsernameNotFoundException("username " + username +" no tiene roles asignados" );
		}
		
		org.springframework.security.core.userdetails.User s = buildUser(user, autorities);
		LOG.info("aut"+s);
		return  s;
		
	}

	private org.springframework.security.core.userdetails.User buildUser(Usuario user,
			List<GrantedAuthority> authorities) {
		LOG.info("buildUser" + authorities);
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getEnabled(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildAuthorities(List<Role> list) {
		LOG.info("entro buildAuthorities"  );
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		for (Role userRole : list) {
			LOG.info("buildAuthorities" + userRole.getAuthority());
			auths.add(new SimpleGrantedAuthority(userRole.getAuthority()));
		}
		LOG.info("buildAuthorities" + auths);
		return new ArrayList<GrantedAuthority>(auths);
	}

}
