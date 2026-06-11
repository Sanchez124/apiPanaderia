package com.example.ApiPanaderia.service;

import com.example.ApiPanaderia.model.Jornada;
import com.example.ApiPanaderia.model.Pedido;
import com.example.ApiPanaderia.repository.JornadaRepository;
import com.example.ApiPanaderia.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JornadaService {

    private final JornadaRepository jornadaRepository;
    private final PedidoRepository pedidoRepository;

    public JornadaService(JornadaRepository jornadaRepository, PedidoRepository pedidoRepository) {
        this.jornadaRepository = jornadaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Jornada> obtenerHistorial() {
        return jornadaRepository.findAllByOrderByFechaAperturaDesc();
    }

    public Optional<Jornada> obtenerJornadaActiva() {
        return jornadaRepository.findByEstado("ABIERTA");
    }

    public Jornada abrirJornada(String username, double montoInicial) {
        Optional<Jornada> activa = obtenerJornadaActiva();
        if (activa.isPresent()) {
            throw new IllegalStateException("Ya existe una jornada abierta. Cierrela antes de abrir una nueva.");
        }

        Jornada nueva = new Jornada(username, montoInicial);
        return jornadaRepository.save(nueva);
    }

    public Jornada cerrarJornada(double montoFinal, String observaciones) {
        Jornada activa = obtenerJornadaActiva()
                .orElseThrow(() -> new IllegalStateException("No hay ninguna jornada activa abierta para cerrar."));

        LocalDateTime ahora = LocalDateTime.now();
        activa.setFechaCierre(ahora);
        activa.setEstado("CERRADA");
        activa.setMontoFinal(montoFinal);
        activa.setObservaciones(observaciones);

        List<Pedido> pedidos = pedidoRepository.findByFechaBetween(activa.getFechaApertura(), ahora);
        
        double totalVentas = pedidos.stream()
                .filter(p -> !"CANCELADO".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(Pedido::getTotal)
                .sum();

        List<String> pedidosIds = pedidos.stream()
                .map(Pedido::getId)
                .collect(Collectors.toList());

        activa.setVentasTotales(totalVentas);
        activa.setPedidosIds(pedidosIds);

        return jornadaRepository.save(activa);
    }
}
