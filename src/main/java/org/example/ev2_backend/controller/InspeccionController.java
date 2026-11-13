package org.example.ev2_backend.controller;

import org.example.ev2_backend.model.Lote;
import org.example.ev2_backend.repository.LoteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InspeccionController {

    private final LoteRepository loteRepository;

    public InspeccionController(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @GetMapping("/productos")
    public List obtenerProductos() {
        return List.of(
                Map.of(
                        "codigo", "PR001",
                        "nombre", "Tela Algodón Premium",
                        "parametros", List.of(
                                Map.of("id", "gramaje", "nombre", "Gramaje", "min", 150, "max", 200, "unidad", "g/m²"),
                                Map.of("id", "resistencia", "nombre", "Resistencia al Rasgado", "min", 20, "max", 50, "unidad", "N")
                        )
                ),
                Map.of(
                        "codigo", "PR003",
                        "nombre", "Mezclilla Denim",
                        "parametros", List.of(
                                Map.of("id", "gramaje", "nombre", "Gramaje", "min", 120, "max", 160, "unidad", "g/m²"),
                                Map.of("id", "encogimiento", "nombre", "Encogimiento", "min", 0, "max", 5, "unidad", "%")
                        )
                )
        );
    }

    @PostMapping("/inspecciones")
    @SuppressWarnings("unchecked")
    public Map<String, Object> registrarInspeccion(@RequestBody Map<String, Object> payload) {
        String codigoLote = (String) payload.get("codigoLote");
        String linea = (String) payload.get("linea");
        String productoCodigo = (String) payload.get("productoCodigo");
        String estado = (String) payload.get("estado");
        Map<String, String> mediciones = (Map<String, String>) payload.get("mediciones");

        Lote lote = loteRepository.findByCodeIgnoreCase(codigoLote).orElse(new Lote());
        lote.setCode(codigoLote);
        lote.setLinea(linea);
        lote.setStatus(estado);
        lote.setProductoNombre(productoCodigo.equals("PR001") ? "Tela Algodón Premium" : "Mezclilla Denim");
        lote.setGramaje(mediciones.containsKey("gramaje") ? Double.parseDouble(mediciones.get("gramaje")) : 0.0);
        lote.setHumedad(12.0); // Valor base por defecto
        lote.setResistencia("Media");
        lote.setObs("Inspección técnica (" + productoCodigo + "): " + mediciones.toString());

        loteRepository.save(lote);

        return Map.of("mensaje", "Inspección registrada", "codigoLote", codigoLote, "estado", estado);
    }
}