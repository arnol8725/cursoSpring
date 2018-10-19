package com.bolsadeideas.springboot.app.models.dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;


@Repository("clienteDaoJPA")
public class ClienteDaoImpl /*implements IClienteDao*/ {
	/*private static final Log LOG = LogFactory.getLog(ClienteDaoImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	

	@SuppressWarnings("unchecked")
	//@Transactional(readOnly= true)
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		
		return em.createQuery("from Cliente").getResultList();
	}


	@Override
	//@Transactional()
	public void save(Cliente cliente) {
		LOG.info("El valor del cliente id "+ cliente.getId());
		if(cliente.getId() != null && cliente.getId() >0) {
			em.merge(cliente);
		} else {
			em.persist(cliente);
		}
		
	}


	@Override
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return (Cliente) em.find(Cliente.class, id);
	}


	@Override
	//@Transactional()
	public void delete(Long id) {
		// TODO Auto-generated method stub
		try {
			em.remove(findOne(id));
		}catch(Exception e) {
			LOG.info("Erro en el metodo delete : "+e.getMessage());
		}
		 
	}
*/
}
