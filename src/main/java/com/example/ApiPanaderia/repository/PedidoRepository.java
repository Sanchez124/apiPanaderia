package com.example.ApiPanaderia.repository;

import com.example.ApiPanaderia.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findByClienteId(String clienteId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByFechaBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
}
