package com.example.ApiPanaderia.controller;

import com.example.ApiPanaderia.model.Jornada;
import com.example.ApiPanaderia.security.RequireRole;
import com.example.ApiPanaderia.service.JornadaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jornadas")
public class JornadaController {

    private final JornadaService jornadaService;

    public JornadaController(JornadaService jornadaService) {
        this.jornadaService = jornadaService;
    }

    public record AbrirJornadaRequest(double montoInicial) {}
    public record CerrarJornadaRequest(double montoFinal, String observaciones) {}

    @GetMapping
    @RequireRole("ADMINISTRADOR")
    public ResponseEntity<List<Jornada>> obtenerHistorial() {
        return ResponseEntity.ok(jornadaService.obtenerHistorial());
    }

    @GetMapping("/activa")
    @RequireRole({"ADMINISTRADOR", "VENTAS"})
    public ResponseEntity<?> obtenerJornadaActiva() {
        return jornadaService.obtenerJornadaActiva()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/abrir")
    @RequireRole({"ADMINISTRADOR", "VENTAS"})
    public ResponseEntity<?> abrirJornada(@RequestBody AbrirJornadaRequest request, HttpServletRequest servletRequest) {
        try {
            String username = (String) servletRequest.getAttribute("username");
            if (username == null) {
                username = "sistema";
            }
            Jornada abierta = jornadaService.abrirJornada(username, request.montoInicial());
            return ResponseEntity.status(HttpStatus.CREATED).body(abierta);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cerrar")
    @RequireRole({"ADMINISTRADOR", "VENTAS"})
    public ResponseEntity<?> cerrarJornada(@RequestBody CerrarJornadaRequest request) {
        try {
            Jornada cerrada = jornadaService.cerrarJornada(request.montoFinal(), request.observaciones());
            return ResponseEntity.ok(cerrada);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
