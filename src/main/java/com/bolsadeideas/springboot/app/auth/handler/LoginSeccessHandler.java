package com.bolsadeideas.springboot.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromPropertyName;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSeccessHandler extends  SimpleUrlAuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager(); 
		FlashMap flashMap = new FlashMap();
		flashMap.put("success", "Hola "+authentication.getName() +",  iniciado session con exito!");
		flashMapManager.saveOutputFlashMap(flashMap, request,response);
		if(authentication != null) {
			logger.info("El usuario '"+authentication.getName()+"' ha iniciado sesion con exito");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
