package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Asociacion;
import com.futbol_club.app.entity.Club;
import com.futbol_club.app.entity.Competicion;
import com.futbol_club.app.entity.Entrenador;
import com.futbol_club.app.repository.AsociacionRepository;
import com.futbol_club.app.repository.ClubRepository;
import com.futbol_club.app.repository.CompeticionRepository;
import com.futbol_club.app.repository.EntrenadorRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/clubes")
public class ClubWebController {
    
    @Autowired
    private ClubRepository clubRepository;
    
    @Autowired
    private AsociacionRepository asociacionRepository;
    
    @Autowired
    private CompeticionRepository competicionRepository;
    
    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("club", new Club());
        // Cargar todas las opciones para las relaciones
        model.addAttribute("todasAsociaciones", asociacionRepository.findAll());
        model.addAttribute("todasCompeticiones", competicionRepository.findAll());
        model.addAttribute("todosEntrenadores", entrenadorRepository.findAll());
        return "club/form";
    }
    
    @PostMapping("/guardar")
    public String guardarClub(@ModelAttribute Club club, 
                             @RequestParam(value = "asociacionId", required = false) Long asociacionId,
                             @RequestParam(value = "competicionIds", required = false) List<Long> competicionIds,
                             @RequestParam(value = "entrenadorId", required = false) Long entrenadorId,
                             RedirectAttributes redirectAttributes) {
        try {
            // Asignar asociación si se seleccionó
            if (asociacionId != null) {
                Asociacion asociacion = asociacionRepository.findById(asociacionId).orElse(null);
                club.setAsociacion(asociacion);
            }
            
            // Asignar competiciones si se seleccionaron
            if (competicionIds != null && !competicionIds.isEmpty()) {
                List<Competicion> competicionesSeleccionadas = competicionRepository.findAllById(competicionIds);
                club.setCompeticiones(competicionesSeleccionadas);
            }
            
            // Asignar entrenador si se seleccionó
            if (entrenadorId != null) {
                Entrenador entrenador = entrenadorRepository.findById(entrenadorId).orElse(null);
                club.setEntrenador(entrenador);
            }
            
            clubRepository.save(club);
            redirectAttributes.addFlashAttribute("success", "Club guardado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el club: " + e.getMessage());
        }
        return "redirect:/web/clubes";
    }

    @GetMapping
    public String listarClubes(@RequestParam(required = false) String search, Model model) {
        List<Club> clubes;
        if (search != null && !search.trim().isEmpty()) {
            clubes = clubRepository.findByNombreContainingIgnoreCaseOrEstadioContainingIgnoreCaseOrCiudadContainingIgnoreCase(
                search, search, search);
            model.addAttribute("search", search);
        } else {
            clubes = clubRepository.findAll();
        }
        model.addAttribute("clubes", clubes);
        return "club/list";
    }
        
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Club club = clubRepository.findById(id).orElse(null);
        model.addAttribute("club", club);
        // Cargar todas las opciones para las relaciones
        model.addAttribute("todasAsociaciones", asociacionRepository.findAll());
        model.addAttribute("todasCompeticiones", competicionRepository.findAll());
        model.addAttribute("todosEntrenadores", entrenadorRepository.findAll());
        return "club/form";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarClub(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Club club = clubRepository.findById(id).orElse(null);
            
            if (club != null) {
                clubRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Club eliminado exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Club no encontrado!");
            }
            
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar el club porque tiene relaciones con otras entidades. " +
                "Primero debe eliminar todos los jugadores y competiciones asociadas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el club: " + e.getMessage());
        }
        
        return "redirect:/web/clubes";
    }
}