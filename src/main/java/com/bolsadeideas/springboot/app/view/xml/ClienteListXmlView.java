package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView{
	
	@Autowired
	public ClienteListXmlView(Marshaller marshaller) {
		super(marshaller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		model.remove("titulo");
		model.remove("page");
		model.remove("clientes");		
		List<Cliente> clientes = (List<Cliente>) model.get("detalleClientes");
		
		model.remove("clientes");
		model.remove("detalleClientes");
		model.put("clienteList", new ClienteList(clientes));

		
		
		super.renderMergedOutputModel(model, request, response);
	}

	
	
}
