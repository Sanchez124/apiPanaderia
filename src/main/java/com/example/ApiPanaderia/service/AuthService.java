package com.example.ApiPanaderia.service;

import com.example.ApiPanaderia.model.TokenSession;
import com.example.ApiPanaderia.model.Usuario;
import com.example.ApiPanaderia.repository.TokenSessionRepository;
import com.example.ApiPanaderia.util.PasswordUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenSessionRepository tokenSessionRepository;

    public AuthService(UsuarioService usuarioService, TokenSessionRepository tokenSessionRepository) {
        this.usuarioService = usuarioService;
        this.tokenSessionRepository = tokenSessionRepository;
    }

    public Optional<TokenSession> login(String username, String password) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorUsername(username);
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();
        boolean passwordValido = PasswordUtils.verifyPassword(password, usuario.getSalt(), usuario.getPassword());
        
        if (!passwordValido) {
            return Optional.empty();
        }

        String token = UUID.randomUUID().toString();
        TokenSession session = new TokenSession(
                token,
                usuario.getUsername(),
                usuario.getRol(),
                LocalDateTime.now().plusHours(24)
        );

        tokenSessionRepository.save(session);
        return Optional.of(session);
    }

    public boolean logout(String token) {
        if (tokenSessionRepository.existsById(token)) {
            tokenSessionRepository.deleteById(token);
            return true;
        }
        return false;
    }
}
