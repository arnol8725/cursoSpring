package com.bolsadeideas.springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {
	
	@Autowired
	@Qualifier("ClienteService")
	private IClienteService clienteServices;


	@GetMapping(value = "/listar" )
	public ClienteList listar() {
		return new	ClienteList(clienteServices.findAll());
	}
}
