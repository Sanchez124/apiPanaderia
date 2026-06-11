package com.example.ApiPanaderia.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;
    private String clienteId;
    private LocalDateTime fecha;
    private String estado; // PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO
    private List<DetallePedido> detalles;
    private double total;

    public Pedido() {}

    public Pedido(String clienteId, List<DetallePedido> detalles) {
        this.clienteId = clienteId;
        this.detalles = detalles;
        this.fecha = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.total = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
        this.total = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
