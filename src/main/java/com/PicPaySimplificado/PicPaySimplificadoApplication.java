package com.PicPaySimplificado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PicPaySimplificadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicPaySimplificadoApplication.class, args);
	}

	// todo - Verificar a conslta ao registrar um user/store, para fazer menos
	// consultas.

}
