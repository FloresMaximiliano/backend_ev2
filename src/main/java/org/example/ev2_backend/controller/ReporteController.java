package org.example.ev2_backend.controller;

import org.example.ev2_backend.model.Lote;
import org.example.ev2_backend.repository.LoteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final LoteRepository loteRepository;

    public ReporteController(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @GetMapping("/kpis")
    public List obtenerKpis () {
        List<Lote> todos = loteRepository.findAll();
        long total = todos.size();
        long aprobados = todos.stream().filter(l -> "Aprobado".equalsIgnoreCase(l.getStatus())).count();
        long rechazados = todos.stream().filter(l -> "Rechazado".equalsIgnoreCase(l.getStatus())).count();

        int pctConformes = total > 0 ? (int) Math.round((aprobados * 100.0) / total) : 0;
        int pctNoConformes = total > 0 ? (int) Math.round((rechazados * 100.0) / total) : 0;

        return List.of(
                Map.of("titulo", "Lotes Conformes", "valor", pctConformes + "%", "detalle", aprobados + " de " + total + " lotes", "tipo", "exito"),
                Map.of("titulo", "Lotes No Conformes", "valor", pctNoConformes + "%", "detalle", rechazados + " de " + total + " lotes", "tipo", "error"),
                Map.of("titulo", "Devoluciones Clientes", "valor", "4%", "detalle", "Meta: < 5%", "tipo", "alerta"),
                Map.of("titulo", "Producción Mensual", "valor", "50.000m", "detalle", "Telas y prendas", "tipo", "info")
        );
    }

    @GetMapping("/desviaciones")
    public List obtenerDesviaciones() {
        return loteRepository.findAll().stream()
                .filter(l -> !"Aprobado".equalsIgnoreCase(l.getStatus()))
                .map(l -> Map.of(
                        "id", l.getCode(),
                        "producto", l.getProductoNombre() != null ? l.getProductoNombre() : "Tela Estándar",
                        "parametro", l.getObs() != null ? l.getObs() : "Gramaje (" + l.getGramaje() + " g/m²)",
                        "estado", l.getStatus()
                ))
                .collect(Collectors.toList());
    }
}