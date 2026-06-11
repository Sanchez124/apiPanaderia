package com.example.ApiPanaderia.controller;

import com.example.ApiPanaderia.model.TokenSession;
import com.example.ApiPanaderia.model.Usuario;
import com.example.ApiPanaderia.security.RequireRole;
import com.example.ApiPanaderia.service.AuthService;
import com.example.ApiPanaderia.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(String username, String password, String nombre, String rol) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.username() == null || request.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username y password son requeridos."));
        }

        java.util.Optional<TokenSession> sessionOpt = authService.login(request.username(), request.password());
        if (sessionOpt.isPresent()) {
            return ResponseEntity.ok(sessionOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas."));
        }
    }

    @PostMapping("/register")
    @RequireRole("ADMINISTRADOR")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest request) {
        if (request.username() == null || request.password() == null || request.rol() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username, password y rol son requeridos."));
        }

        String rolUpper = request.rol().toUpperCase();
        if (!rolUpper.equals("ADMINISTRADOR") && !rolUpper.equals("VENTAS") && !rolUpper.equals("INVENTARIO")) {
            return ResponseEntity.badRequest().body(Map.of("error", "El rol debe ser ADMINISTRADOR, VENTAS o INVENTARIO."));
        }

        if (usuarioService.existeUsername(request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El nombre de usuario ya existe."));
        }

        Usuario nuevoUsuario = new Usuario(
                request.username(),
                request.password(),
                null,
                request.nombre() != null ? request.nombre() : request.username(),
                rolUpper
        );

        Usuario guardado = usuarioService.crearUsuario(nuevoUsuario);
        guardado.setPassword(null);
        guardado.setSalt(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            if (authService.logout(token)) {
                return ResponseEntity.ok(Map.of("message", "Sesión cerrada exitosamente."));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No se pudo cerrar sesión. Token inválido o ausente."));
    }
}
