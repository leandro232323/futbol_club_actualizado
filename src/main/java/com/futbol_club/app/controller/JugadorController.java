package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Jugador;
import com.futbol_club.app.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @GetMapping
    public List<Jugador> getAllJugadores() {
        return jugadorRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Jugador getJugadorById(@PathVariable Long id) {
        return jugadorRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Jugador createJugador(@RequestBody Jugador jugador) {
        return jugadorRepository.save(jugador);
    }
    
    @PutMapping("/{id}")
    public Jugador updateJugador(@PathVariable Long id, @RequestBody Jugador jugadorDetails) {
        Jugador jugador = jugadorRepository.findById(id).orElse(null);
        if (jugador != null) {
            jugador.setNombre(jugadorDetails.getNombre());
            jugador.setApellido(jugadorDetails.getApellido());
            jugador.setNumero(jugadorDetails.getNumero());
            jugador.setPosicion(jugadorDetails.getPosicion());
            jugador.setEdad(jugadorDetails.getEdad());
            jugador.setNacionalidad(jugadorDetails.getNacionalidad());
            jugador.setValorMercado(jugadorDetails.getValorMercado());
            jugador.setFechaNacimiento(jugadorDetails.getFechaNacimiento());
            jugador.setFechaContrato(jugadorDetails.getFechaContrato());
            jugador.setClub(jugadorDetails.getClub());
            return jugadorRepository.save(jugador);
        }
        return null;
    }
    
    @DeleteMapping("/{id}")
    public void deleteJugador(@PathVariable Long id) {
        jugadorRepository.deleteById(id);
    }
}