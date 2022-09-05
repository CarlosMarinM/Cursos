package com.udemy.microservices.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.udemy.microservices.commons.usuarios.models.entity"})
public class ServicioUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioUsuariosApplication.class, args);
	}

}
