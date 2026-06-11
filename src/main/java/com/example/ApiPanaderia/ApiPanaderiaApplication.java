package com.example.ApiPanaderia;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.ApiPanaderia.model.Usuario;
import com.example.ApiPanaderia.service.UsuarioService;

@SpringBootApplication
public class ApiPanaderiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiPanaderiaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UsuarioService usuarioService) {
		return args -> {
			if (usuarioService.obtenerPorUsername("admin").isEmpty()) {
				Usuario admin = new Usuario(
						"admin",
						"admin123",
						null,
						"Administrador Inicial",
						"ADMINISTRADOR"
				);
				usuarioService.crearUsuario(admin);
				System.out.println("----------------------------------------");
				System.out.println("SE HA CREADO EL USUARIO ADMINISTRADOR POR DEFECTO");
				System.out.println("Usuario: admin");
				System.out.println("Contraseña: admin123");
				System.out.println("Rol: ADMINISTRADOR");
				System.out.println("----------------------------------------");
			}
		};
	}
}
