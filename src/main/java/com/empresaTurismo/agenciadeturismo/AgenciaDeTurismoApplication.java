package com.empresaTurismo.agenciadeturismo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgenciaDeTurismoApplication {

     @Value("${file.upload-dir}")
    private String uploadDir;
    
	public static void main(String[] args) {
		SpringApplication.run(AgenciaDeTurismoApplication.class, args);

        }

}
