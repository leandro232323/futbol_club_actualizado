package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Competicion;
import com.futbol_club.app.repository.CompeticionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/competiciones")
public class CompeticionController {
    
    @Autowired
    private CompeticionRepository competicionRepository;
    
    @GetMapping
    public List<Competicion> getAllCompeticiones() {
        return competicionRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Competicion getCompeticionById(@PathVariable Long id) {
        return competicionRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Competicion createCompeticion(@RequestBody Competicion competicion) {
        return competicionRepository.save(competicion);
    }
    
    @PutMapping("/{id}")
    public Competicion updateCompeticion(@PathVariable Long id, @RequestBody Competicion competicionDetails) {
        Competicion competicion = competicionRepository.findById(id).orElse(null);
        if (competicion != null) {
            competicion.setNombre(competicionDetails.getNombre());
            competicion.setMontoPremio(competicionDetails.getMontoPremio());
            competicion.setFechaInicio(competicionDetails.getFechaInicio());
            competicion.setFechaFin(competicionDetails.getFechaFin());
            return competicionRepository.save(competicion);
        }
        return null;
    }
    
    @DeleteMapping("/{id}")
    public void deleteCompeticion(@PathVariable Long id) {
        competicionRepository.deleteById(id);
    }
}