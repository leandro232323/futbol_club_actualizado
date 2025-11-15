package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Asociacion;
import com.futbol_club.app.repository.AsociacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/asociaciones")
public class AsociacionController {
    
    @Autowired
    private AsociacionRepository asociacionRepository;
    
    @GetMapping
    public List<Asociacion> getAllAsociaciones() {
        return asociacionRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Asociacion getAsociacionById(@PathVariable Long id) {
        return asociacionRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Asociacion createAsociacion(@RequestBody Asociacion asociacion) {
        return asociacionRepository.save(asociacion);
    }
    
    @PutMapping("/{id}")
    public Asociacion updateAsociacion(@PathVariable Long id, @RequestBody Asociacion asociacionDetails) {
        Asociacion asociacion = asociacionRepository.findById(id).orElse(null);
        if (asociacion != null) {
            asociacion.setNombre(asociacionDetails.getNombre());
            asociacion.setPais(asociacionDetails.getPais());
            asociacion.setPresidente(asociacionDetails.getPresidente());
            return asociacionRepository.save(asociacion);
        }
        return null;
    }
    
    @DeleteMapping("/{id}")
    public void deleteAsociacion(@PathVariable Long id) {
        asociacionRepository.deleteById(id);
    }
}