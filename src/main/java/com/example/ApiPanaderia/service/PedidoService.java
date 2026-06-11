package com.example.ApiPanaderia.service;

import com.example.ApiPanaderia.model.Pedido;
import com.example.ApiPanaderia.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPorId(String id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerPorCliente(String clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public Pedido crear(Pedido pedido) {
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        if (pedido.getDetalles() != null) {
            double total = pedido.getDetalles().stream()
                    .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                    .sum();
            pedido.setTotal(total);
        }
        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> actualizarEstado(String id, String nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        });
    }

    public Optional<Pedido> actualizar(String id, Pedido pedidoActualizado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setClienteId(pedidoActualizado.getClienteId());
            pedido.setDetalles(pedidoActualizado.getDetalles());
            pedido.setEstado(pedidoActualizado.getEstado());
            if (pedidoActualizado.getDetalles() != null) {
                double total = pedidoActualizado.getDetalles().stream()
                        .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                        .sum();
                pedido.setTotal(total);
            }
            return pedidoRepository.save(pedido);
        });
    }

    public boolean eliminar(String id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
