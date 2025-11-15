package com.futbol_club.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "jugadores")
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String apellido;
    private int numero;
    private String posicion;
    private LocalDate fechaNacimiento;
    private int edad;
    private String nacionalidad;
    private double valorMercado;
    private LocalDate fechaContrato;
    
    // Relación ManyToOne con Club
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;
    
    // Constructores
    public Jugador() {}
    
    public Jugador(String nombre, String apellido, int numero, String posicion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numero = numero;
        this.posicion = posicion;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public double getValorMercado() { return valorMercado; }
    public void setValorMercado(double valorMercado) { this.valorMercado = valorMercado; }
    
    public LocalDate getFechaContrato() { return fechaContrato; }
    public void setFechaContrato(LocalDate fechaContrato) { this.fechaContrato = fechaContrato; }
    
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}