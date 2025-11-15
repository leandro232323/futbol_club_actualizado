package com.futbol_club.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "entrenadores")
public class Entrenador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String apellido;
    private int edad;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String estiloJuego;
    private int anosExperiencia;
    
    // Cambiar a EAGER para cargar automáticamente
    @OneToOne(mappedBy = "entrenador", fetch = FetchType.EAGER)
    private Club club;
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getEstiloJuego() { return estiloJuego; }
    public void setEstiloJuego(String estiloJuego) { this.estiloJuego = estiloJuego; }
    
    public int getAnosExperiencia() { return anosExperiencia; }
    public void setAnosExperiencia(int anosExperiencia) { this.anosExperiencia = anosExperiencia; }
    
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}