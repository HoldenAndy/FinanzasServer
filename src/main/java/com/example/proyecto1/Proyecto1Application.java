package com.example.proyecto1;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class Proyecto1Application {

	public static void main(String[] args) {
		SpringApplication.run(Proyecto1Application.class, args);
	}
	@PostConstruct
	public void init() {
		// Fuerza a toda la aplicación Java a usar hora Perú
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		System.out.println("🕒 Zona horaria forzada a: " + new java.util.Date());
	}
}
