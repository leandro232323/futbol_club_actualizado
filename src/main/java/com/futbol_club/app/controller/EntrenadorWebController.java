package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Club;
import com.futbol_club.app.entity.Entrenador;
import com.futbol_club.app.repository.ClubRepository;
import com.futbol_club.app.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/web/entrenadores")
public class EntrenadorWebController {
    
    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    @Autowired
    private ClubRepository clubRepository;
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        // Cargar todos los clubes disponibles para asociar
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "entrenador/form";
    }
    
    @PostMapping("/guardar")
    public String guardarEntrenador(@ModelAttribute Entrenador entrenador, 
                                   @RequestParam(value = "clubId", required = false) Long clubId,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Si se seleccionó un club, actualizar la relación
            if (clubId != null) {
                Club club = clubRepository.findById(clubId).orElse(null);
                // Primero guardar el entrenador
                Entrenador entrenadorGuardado = entrenadorRepository.save(entrenador);
                // Luego actualizar el club con el entrenador
                if (club != null) {
                    club.setEntrenador(entrenadorGuardado);
                    clubRepository.save(club);
                }
            } else {
                // Guardar sin club
                entrenadorRepository.save(entrenador);
            }
            
            redirectAttributes.addFlashAttribute("success", "Entrenador guardado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el entrenador: " + e.getMessage());
        }
        return "redirect:/web/entrenadores";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Entrenador entrenador = entrenadorRepository.findByIdWithClub(id).orElse(null);
        model.addAttribute("entrenador", entrenador);
        // Cargar todos los clubes disponibles
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "entrenador/form";
    }

    @GetMapping
    public String listarEntrenadores(@RequestParam(required = false) String search, Model model) {
        List<Entrenador> entrenadores;
        
        if (search != null && !search.trim().isEmpty()) {
            // Usar el nuevo método con JOIN FETCH
            entrenadores = entrenadorRepository.findBySearchTermWithClub(search);
            model.addAttribute("search", search);
        } else {
            // Usar el nuevo método con JOIN FETCH
            entrenadores = entrenadorRepository.findAllWithClub();
        }
        model.addAttribute("entrenadores", entrenadores);
        return "entrenador/list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarEntrenador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Entrenador entrenador = entrenadorRepository.findByIdWithClub(id).orElse(null);
            
            if (entrenador != null) {
                // Verificar si está asociado a un club
                if (entrenador.getClub() != null) {
                    redirectAttributes.addFlashAttribute("error", 
                        "No se puede eliminar el entrenador '" + entrenador.getNombre() + " " + entrenador.getApellido() + 
                        "' porque está asociado al club: " + entrenador.getClub().getNombre() + 
                        ". Primero debe desvincularlo del club.");
                    return "redirect:/web/entrenadores";
                }
                
                entrenadorRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Entrenador eliminado exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Entrenador no encontrado!");
            }
            
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar el entrenador porque está asociado a un club. " +
                "Primero debe desvincularlo del club.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el entrenador: " + e.getMessage());
        }
        
        return "redirect:/web/entrenadores";
    }
}