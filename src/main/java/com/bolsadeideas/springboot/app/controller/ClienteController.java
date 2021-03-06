package com.bolsadeideas.springboot.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import com.bolsadeideas.springboot.app.view.xml.ClienteList;


@Controller
public class ClienteController {
	private static final Log LOG = LogFactory.getLog(ClienteController.class);

	/*
	 * @Autowired
	 * 
	 * @Qualifier("clienteDaoJPA") private IClienteDao clienteDao;
	 */

	@Autowired
	@Qualifier("ClienteService")
	private IClienteService clienteServices;

	@Autowired
	@Qualifier("uploadFileService")
	private IUploadFileService uploadFileService;
	
	@Autowired
	private MessageSource messageSource;

	@Secured({"ROLE_USER"})
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	//@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		LOG.info("El valor del cliente id " + id);
		Cliente cliente = null;

		if (id > 0) {
			// cliente = clienteService.findOne(id);
			cliente = clienteServices.fetchByIdWithFacturas(id);
			LOG.info("El valor del cliente editar id " + cliente.getId());
			if (cliente == null) {
				flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser cero!");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Consultar Cliente");
		return "ver";
	}
	
	@GetMapping(value = "/listar-Rest" )
	public @ResponseBody ClienteList listarRest() {
		return 	new ClienteList(clienteServices.findAll());
	}

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication,HttpServletRequest request,Locale locale) {

		/*
		 * ,Authentication authentication if (authentication != null) {
		 * LOG.info("Hola autentificado, tu session es ".concat(authentication.getName()
		 * )); }
		 */

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			LOG.info("Hola autentificado,Usuario autenticado, username  es ".concat(auth.getName()));
		}

		if (hasRole("ROLE_ADMIN")) {
			LOG.info("Hola ".concat(auth.getName()).concat(" tienes acceso!!!"));
		} else {
			LOG.info("Hola ".concat(auth.getName()).concat(" no tienes acceso"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
		if(securityContext.isUserInRole("ROLE_ADMIN")){
			LOG.info("forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat("  tienes acceso"));
		}else{
			LOG.info("forma usando SecurityContextHolderAwareRequestWrapper Hola ".concat(auth.getName()).concat(" no tienes acceso"));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")){
			LOG.info("forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat("  tienes acceso"));
		}else{
			LOG.info("forma usando HttpServletRequest Hola ".concat(auth.getName()).concat(" no tienes acceso"));
		}
		// Pageable pageRequest = new PageRequest.of(, size) esto para sprint boot 2
		Pageable pageRequest = new PageRequest(page, 5);
		Page<Cliente> clientes = clienteServices.findAll(pageRequest);
		List<Cliente> DetalleClientes = clienteServices.findAll();
		PageRender<Cliente> pageRender = new PageRender("listar", clientes);

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		// model.addAttribute("clientes",clienteServices.findAll());
		model.addAttribute("clientes", clientes);
		model.addAttribute("detalleClientes", DetalleClientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}

		if (!foto.isEmpty()) {
			LOG.info("valor de id :" + cliente.getId());
			LOG.info("valor de foto :" + cliente.getFoto());

			if (cliente.getId() != null && cliente.getId() > 0 && foto != null && foto.getName().length() > 0) {
				LOG.info("entro");
				Cliente clientes = clienteServices.findOne(cliente.getId());

				if (clientes.getFoto() != null && clientes.getFoto().length() > 0) {

					uploadFileService.delete(clientes.getFoto());

				}

			}
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
			// String rootPath = "C://Temp//uploads";
			// String rootPath = directorioRecursos.toFile().getAbsolutePath();
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

			LOG.info("El valor de la ruta del archivo:" + foto.getOriginalFilename());
			cliente.setFoto(uniqueFilename);
		}

		LOG.info("El valor del cliente id " + cliente.getId());
		LOG.info("El valor del cliente id " + cliente.getNombre());
		clienteServices.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente creado con exito");
		return "redirect:listar";
	}
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		LOG.info("En el metodo eliminar id =" + id);

		if (id > 0) {
			// clienteService.delete(id);
			Cliente cliente = clienteServices.findOne(id);
			clienteServices.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado  con exito");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto fue eliminado: " + cliente.getFoto());
			}

		}

		return "redirect:/listar";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		LOG.info("El valor del cliente id " + id);
		Cliente cliente = null;

		if (id > 0) {
			// cliente = clienteService.findOne(id);
			cliente = clienteServices.findOne(id);
			LOG.info("El valor del cliente editar id " + cliente.getId());
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no puede ser cero!");
				return "form";
			}

		} else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}

	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		/*for (GrantedAuthority authority : authorities) {

			if (role.equals(authority.getAuthority())) {
				LOG.info("Hola usuario ".concat(auth.getName()).concat(" tu role es".concat(authority.getAuthority())));
				return true;
			}
		}

		return false;*/
	}

}
