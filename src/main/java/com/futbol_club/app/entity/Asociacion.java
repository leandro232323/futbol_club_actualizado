package com.futbol_club.app.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "asociaciones")
public class Asociacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String pais;
    private String presidente;
    
    // Relación OneToMany con Club
    @OneToMany(mappedBy = "asociacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Club> clubes;
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
    
    public List<Club> getClubes() { return clubes; }
    public void setClubes(List<Club> clubes) { this.clubes = clubes; }
}