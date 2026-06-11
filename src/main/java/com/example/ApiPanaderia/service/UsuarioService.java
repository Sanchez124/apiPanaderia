package com.example.ApiPanaderia.service;

import com.example.ApiPanaderia.model.Usuario;
import com.example.ApiPanaderia.repository.UsuarioRepository;
import com.example.ApiPanaderia.util.PasswordUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario crearUsuario(Usuario usuario) {
        String salt = PasswordUtils.getSalt();
        String hashedPassword = PasswordUtils.hashPassword(usuario.getPassword(), salt);
        usuario.setSalt(salt);
        usuario.setPassword(hashedPassword);
        return usuarioRepository.save(usuario);
    }

    public boolean existeUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }
}
