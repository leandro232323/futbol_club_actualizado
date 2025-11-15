package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Entrenador;
import com.futbol_club.app.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {
    
    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    @GetMapping
    public List<Entrenador> getAllEntrenadores() {
        return entrenadorRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Entrenador getEntrenadorById(@PathVariable Long id) {
        return entrenadorRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Entrenador createEntrenador(@RequestBody Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }
    
    @PutMapping("/{id}")
    public Entrenador updateEntrenador(@PathVariable Long id, @RequestBody Entrenador entrenadorDetails) {
        Entrenador entrenador = entrenadorRepository.findById(id).orElse(null);
        if (entrenador != null) {
            entrenador.setNombre(entrenadorDetails.getNombre());
            entrenador.setApellido(entrenadorDetails.getApellido());
            entrenador.setEdad(entrenadorDetails.getEdad());
            entrenador.setNacionalidad(entrenadorDetails.getNacionalidad());
            return entrenadorRepository.save(entrenador);
        }
        return null;
    }
    
    @DeleteMapping("/{id}")
    public void deleteEntrenador(@PathVariable Long id) {
        entrenadorRepository.deleteById(id);
    }
}