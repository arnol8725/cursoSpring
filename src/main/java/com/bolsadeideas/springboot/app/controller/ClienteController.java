package com.bolsadeideas.springboot.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.dao.Impl.ClienteDaoImpl;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
public class ClienteController {
	private static final Log LOG = LogFactory.getLog(ClienteController.class);
	
	
	/*@Autowired
	@Qualifier("clienteDaoJPA")
	private IClienteDao clienteDao;*/
	
	@Autowired
	@Qualifier("ClienteService")
	private IClienteService clienteServices;
	
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo","Listado de clientes");		
		model.addAttribute("clientes",clienteServices.findAll());
		return "listar"; 
	}
	
	@RequestMapping(value="/form")
	public String crear(Map<String,Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,@RequestParam("file") MultipartFile foto , RedirectAttributes flash, SessionStatus status) {
	
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		if (!foto.isEmpty()) {

			String rootPath = "C://Temp//uploads";

			try {

				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Has subido correctamente '" + foto.getOriginalFilename() + "'");

				cliente.setFoto(foto.getOriginalFilename());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		LOG.info("El valor del cliente id "+ cliente.getId());
		LOG.info("El valor del cliente id "+ cliente.getNombre());
		clienteServices.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success","Cliente creado con exito");
		return "redirect:listar";
	}
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		LOG.info("En el metodo eliminar id ="+id);
		
		if(id > 0) {
			//clienteService.delete(id);
			clienteServices.delete(id);
			flash.addFlashAttribute("success","Cliente eliminado  con exito");
		}
		
		return "redirect:/listar";
	}
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		LOG.info("El valor del cliente id "+ id);
		Cliente cliente = null;
		
		if(id > 0) {
			//cliente = clienteService.findOne(id);
			cliente = clienteServices.findOne(id);
			LOG.info("El valor del cliente editar id "+ cliente.getId());
			if (cliente == null) {
				flash.addFlashAttribute("error","El id del cliente no puede ser cero!");
				return "form";
			}
		
		} else {
			flash.addFlashAttribute("error","El id del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
}
