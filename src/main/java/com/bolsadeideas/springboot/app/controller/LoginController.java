package com.bolsadeideas.springboot.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String Login(@RequestParam(value="error",required=false) String error,@RequestParam(value="logout",required=false) String logout, Model model, Principal principal,RedirectAttributes flash) {
		if(principal != null) {
			flash.addFlashAttribute("info","Ya ha iniciado sesion anteriormente");
			return "redirect:/";
		}
		
		if (error != null) {
			model.addAttribute("error","error en el login: Nombre de usuario o contraseña incorrecto, por favor vuelve a intentarlo!");
		}
		if (logout != null) {
			model.addAttribute("success","Ha cerrado sesión con exito!");
		}
		return "login";
	}
	
	
}
