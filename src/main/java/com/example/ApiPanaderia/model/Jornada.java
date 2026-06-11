package com.example.ApiPanaderia.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "jornadas")
public class Jornada {

    @Id
    private String id;
    private String usuarioResponsable;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private double montoInicial;
    private double montoFinal;
    private double ventasTotales;
    private List<String> pedidosIds;
    private String estado; // "ABIERTA", "CERRADA"
    private String observaciones;

    public Jornada() {}

    public Jornada(String usuarioResponsable, double montoInicial) {
        this.usuarioResponsable = usuarioResponsable;
        this.montoInicial = montoInicial;
        this.fechaApertura = LocalDateTime.now();
        this.estado = "ABIERTA";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioResponsable() { return usuarioResponsable; }
    public void setUsuarioResponsable(String usuarioResponsable) { this.usuarioResponsable = usuarioResponsable; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public double getMontoInicial() { return montoInicial; }
    public void setMontoInicial(double montoInicial) { this.montoInicial = montoInicial; }

    public double getMontoFinal() { return montoFinal; }
    public void setMontoFinal(double montoFinal) { this.montoFinal = montoFinal; }

    public double getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(double ventasTotales) { this.ventasTotales = ventasTotales; }

    public List<String> getPedidosIds() { return pedidosIds; }
    public void setPedidosIds(List<String> pedidosIds) { this.pedidosIds = pedidosIds; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
