package com.example.ApiPanaderia.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tokens")
public class TokenSession {

    @Id
    private String id;
    private String username;
    private String rol;
    private LocalDateTime fechaExpiracion;

    public TokenSession() {}

    public TokenSession(String id, String username, String rol, LocalDateTime fechaExpiracion) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}
