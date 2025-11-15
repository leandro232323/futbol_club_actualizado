package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Club;
import com.futbol_club.app.entity.Competicion;
import com.futbol_club.app.repository.ClubRepository;
import com.futbol_club.app.repository.CompeticionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/web/competiciones")
public class CompeticionWebController {
    
    @Autowired
    private CompeticionRepository competicionRepository;
    
    @Autowired
    private ClubRepository clubRepository;
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("competicion", new Competicion());
        // Cargar todos los clubes disponibles para asociar
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "competicion/form";
    }
    
    @PostMapping("/guardar")
    public String guardarCompeticion(@ModelAttribute Competicion competicion, 
                                    @RequestParam(value = "clubIds", required = false) List<Long> clubIds,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Asignar clubes si se seleccionaron
            if (clubIds != null && !clubIds.isEmpty()) {
                List<Club> clubesSeleccionados = clubRepository.findAllById(clubIds);
                competicion.setClubes(clubesSeleccionados);
            }
            
            competicionRepository.save(competicion);
            redirectAttributes.addFlashAttribute("success", "Competición guardada exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la competición: " + e.getMessage());
        }
        return "redirect:/web/competiciones";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Competicion competicion = competicionRepository.findByIdWithClubes(id).orElse(null);
        model.addAttribute("competicion", competicion);
        // Cargar todos los clubes disponibles
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "competicion/form";
    }

    @GetMapping
    public String listarCompeticiones(@RequestParam(required = false) String search, Model model) {
        List<Competicion> competiciones;
        if (search != null && !search.trim().isEmpty()) {
            competiciones = competicionRepository.findByNombreContainingIgnoreCase(search);
            model.addAttribute("search", search);
        } else {
            competiciones = competicionRepository.findAllWithClubes();
        }
        model.addAttribute("competiciones", competiciones);
        return "competicion/list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCompeticion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Competicion competicion = competicionRepository.findByIdWithClubes(id).orElse(null);
            
            if (competicion != null) {
                // Verificar si hay clubes asociados
                List<Club> clubesConEstaCompeticion = clubRepository.findByCompeticionesId(id);
                
                if (!clubesConEstaCompeticion.isEmpty()) {
                    // Hay clubes asociados, no se puede eliminar
                    StringBuilder clubesNombres = new StringBuilder();
                    for (Club club : clubesConEstaCompeticion) {
                        if (clubesNombres.length() > 0) clubesNombres.append(", ");
                        clubesNombres.append(club.getNombre());
                    }
                    
                    redirectAttributes.addFlashAttribute("error", 
                        "No se puede eliminar la competición '" + competicion.getNombre() + 
                        "' porque está asociada a los siguientes clubes: " + clubesNombres.toString() +
                        ". Primero debe desvincular la competición de estos clubes.");
                    return "redirect:/web/competiciones";
                }
                
                // No hay clubes asociados, se puede eliminar
                competicionRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Competición eliminada exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Competición no encontrada!");
            }
            
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar la competición porque está siendo utilizada por uno o más clubes. " +
                "Primero debe desvincularla de todos los clubes.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la competición: " + e.getMessage());
        }
        
        return "redirect:/web/competiciones";
    }
}