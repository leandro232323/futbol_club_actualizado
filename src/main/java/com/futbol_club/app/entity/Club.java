package com.futbol_club.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clubes")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private LocalDate fechaFundacion;
    private String estadio;
    private String ciudad;
    private String presidente;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrenador_id")
    private Entrenador entrenador;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private List<Jugador> jugadores;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asociacion_id")
    private Asociacion asociacion;
    
    // CAMBIO: Agregar mappedBy para que Club NO sea el dueño
    @ManyToMany(mappedBy = "clubes", fetch = FetchType.LAZY)
    private List<Competicion> competiciones;
    
    // Constructores
    public Club() {}
    
    public Club(String nombre) {
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public LocalDate getFechaFundacion() { return fechaFundacion; }
    public void setFechaFundacion(LocalDate fechaFundacion) { this.fechaFundacion = fechaFundacion; }
    
    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }
    
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    
    public String getPresidente() { return presidente; }
    public void setPresidente(String presidente) { this.presidente = presidente; }
    
    public Entrenador getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }
    
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    
    public Asociacion getAsociacion() { return asociacion; }
    public void setAsociacion(Asociacion asociacion) { this.asociacion = asociacion; }
    
    public List<Competicion> getCompeticiones() { return competiciones; }
    public void setCompeticiones(List<Competicion> competiciones) { this.competiciones = competiciones; }
}