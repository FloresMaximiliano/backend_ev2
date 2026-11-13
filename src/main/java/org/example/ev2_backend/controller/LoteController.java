package org.example.ev2_backend.controller;

import org.example.ev2_backend.model.Lote;
import org.example.ev2_backend.repository.LoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteRepository loteRepository;

    public LoteController(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    // 1. Listar todos (LotesAdmin)
    @GetMapping
    public List<Lote> listarTodos() {
        return loteRepository.findAll();
    }

    // 2. Buscar por código para Supervisor.jsx
    @GetMapping("/{codigo}")
    public ResponseEntity buscarPorCodigo(
            @PathVariable String codigo,
            @RequestParam(required = false) String linea) {

        return loteRepository.findByCodeIgnoreCase(codigo)
                .map(lote -> {
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("codigo", lote.getCode());
                    respuesta.put("linea", linea != null ? linea : lote.getLinea());
                    respuesta.put("estado", lote.getStatus());
                    respuesta.put("resultado", lote.getObs() != null && !lote.getObs().isEmpty()
                            ? lote.getObs()
                            : "Dentro de los parámetros establecidos.");
                    return ResponseEntity.ok(respuesta);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear lote (LotesAdmin)
    @PostMapping
    public Lote crear(@RequestBody Lote lote) {
        if (lote.getLinea() == null) lote.setLinea("Línea 1");
        return loteRepository.save(lote);
    }

    // 4. Actualizar lote (LotesAdmin)
    @PutMapping("/{id}")
    public ResponseEntity<Lote> actualizar(@PathVariable Long id, @RequestBody Lote datos) {
        return loteRepository.findById(id)
                .map(lote -> {
                    lote.setCode(datos.getCode());
                    lote.setStatus(datos.getStatus());
                    lote.setHumedad(datos.getHumedad());
                    lote.setGramaje(datos.getGramaje());
                    lote.setResistencia(datos.getResistencia());
                    lote.setObs(datos.getObs());
                    return ResponseEntity.ok(loteRepository.save(lote));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Eliminar lote (LotesAdmin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!loteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        loteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}