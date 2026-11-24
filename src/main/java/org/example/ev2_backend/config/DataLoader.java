package org.example.ev2_backend.config;

import org.example.ev2_backend.model.Lote;
import org.example.ev2_backend.model.Usuario;
import org.example.ev2_backend.repository.LoteRepository;
import org.example.ev2_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosIniciales(
            LoteRepository loteRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (loteRepository.count() == 0) {
                Lote l1 = new Lote();
                l1.setCode("LOT-2026-001");
                l1.setStatus("Rechazado");
                l1.setHumedad(15.2);
                l1.setGramaje(142.0);
                l1.setResistencia("Baja");
                l1.setLinea("Línea 1");
                l1.setProductoNombre("Tela de algodón 100%");
                l1.setObs("Exceso de humedad y gramaje fuera de norma (142 g/m²).");

                Lote l2 = new Lote();
                l2.setCode("LOT-2026-002");
                l2.setStatus("Aprobado");
                l2.setHumedad(11.5);
                l2.setGramaje(175.0);
                l2.setResistencia("Alta");
                l2.setLinea("Línea 3");
                l2.setProductoNombre("Tela Algodón Premium");
                l2.setObs("Dentro de los parámetros establecidos.");

                Lote l3 = new Lote();
                l3.setCode("LOT-2026-003");
                l3.setStatus("Pendiente");
                l3.setHumedad(12.0);
                l3.setGramaje(155.0);
                l3.setResistencia("Media");
                l3.setLinea("Línea 2");
                l3.setProductoNombre("Mezclilla Denim");
                l3.setObs("En espera de prueba de resistencia en laboratorio.");

                loteRepository.save(l1);
                loteRepository.save(l2);
                loteRepository.save(l3);
            }

            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("admin", passwordEncoder.encode("admin123"), "Administrador General", "ADMIN"));
                usuarioRepository.save(new Usuario("supervisor", passwordEncoder.encode("super123"), "Supervisor de Planta", "SUPERVISOR"));
                usuarioRepository.save(new Usuario("inspector", passwordEncoder.encode("insp123"), "Inspector de Calidad", "INSPECTOR"));
            }
        };
    }
}