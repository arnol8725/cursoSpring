package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDatasJpaApplication implements CommandLineRunner {
	
	@Autowired
	@Qualifier("uploadFileService")
	private IUploadFileService uploadFileService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDatasJpaApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
		String password = "12345";
				for(int i=0; i<2 ; i++) {
					String bycrytPassword = passwordEncoder.encode(password);
					System.out.println(bycrytPassword);
				}
			
		
	}
	
	
	
}
