package org.example.ev2_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lotes")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // Ej: LOT-2026-001

    private String status;      // Aprobado, Rechazado, Pendiente, Observado
    private Double humedad;
    private Double gramaje;
    private String resistencia; // Alta, Media, Baja
    private String obs;         // Observación o resultado del ensayo
    private String linea;       // Línea 1, Línea 2, etc.
    private String productoNombre;

    public Lote() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getHumedad() { return humedad; }
    public void setHumedad(Double humedad) { this.humedad = humedad; }

    public Double getGramaje() { return gramaje; }
    public void setGramaje(Double gramaje) { this.gramaje = gramaje; }

    public String getResistencia() { return resistencia; }
    public void setResistencia(String resistencia) { this.resistencia = resistencia; }

    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }

    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
}