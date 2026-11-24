package org.example.ev2_backend.controller;

import org.example.ev2_backend.model.Usuario;
import org.example.ev2_backend.repository.UsuarioRepository;
import org.example.ev2_backend.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();
        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol(), usuario.getNombreCompleto());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "username", usuario.getUsername(),
                "nombre", usuario.getNombreCompleto(),
                "rol", usuario.getRol()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String nombre = request.getOrDefault("nombreCompleto", username);
        String rol = request.getOrDefault("rol", "SUPERVISOR").toUpperCase();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario y contraseña obligatorios"));
        }

        if (usuarioRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El usuario ya existe"));
        }

        Usuario nuevo = new Usuario(username, passwordEncoder.encode(password), nombre, rol);
        usuarioRepository.save(nuevo);

        String token = jwtService.generarToken(nuevo.getUsername(), nuevo.getRol(), nuevo.getNombreCompleto());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "token", token,
                "username", nuevo.getUsername(),
                "nombre", nuevo.getNombreCompleto(),
                "rol", nuevo.getRol()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> perfilActual(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No autenticado"));
        }
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "username", usuario.getUsername(),
                "nombre", usuario.getNombreCompleto(),
                "rol", usuario.getRol()
        ));
    }
}