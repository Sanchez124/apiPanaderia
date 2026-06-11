package com.example.ApiPanaderia.repository;

import com.example.ApiPanaderia.model.Jornada;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JornadaRepository extends MongoRepository<Jornada, String> {
    Optional<Jornada> findByEstado(String estado);
    List<Jornada> findAllByOrderByFechaAperturaDesc();
}
