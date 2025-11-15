package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Asociacion;
import com.futbol_club.app.entity.Club;
import com.futbol_club.app.repository.AsociacionRepository;
import com.futbol_club.app.repository.ClubRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/asociaciones")
public class AsociacionWebController {
    
    @Autowired
    private AsociacionRepository asociacionRepository;
    
    @Autowired
    private ClubRepository clubRepository;

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("asociacion", new Asociacion());
        // Cargar todos los clubes disponibles para asociar
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "asociacion/form";
    }

    @PostMapping("/guardar")
    public String guardarAsociacion(@ModelAttribute Asociacion asociacion, 
                                @RequestParam(value = "clubIds", required = false) List<Long> clubIds,
                                RedirectAttributes redirectAttributes) {
        try {
            // Guardar la asociación primero
            Asociacion asociacionGuardada = asociacionRepository.save(asociacion);
            
            // PRIMERO: Desvincular todos los clubes actuales de esta asociación
            List<Club> clubesActuales = clubRepository.findByAsociacionId(asociacionGuardada.getId());
            for (Club club : clubesActuales) {
                club.setAsociacion(null);
                clubRepository.save(club);
            }
            
            // SEGUNDO: Asociar los nuevos clubes seleccionados
            if (clubIds != null && !clubIds.isEmpty()) {
                List<Club> clubesSeleccionados = clubRepository.findAllById(clubIds);
                for (Club club : clubesSeleccionados) {
                    club.setAsociacion(asociacionGuardada);
                    clubRepository.save(club);
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Asociación guardada exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la asociación: " + e.getMessage());
        }
        return "redirect:/web/asociaciones";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        // CAMBIAR: Usar findByIdWithClubes en lugar de findById
        Asociacion asociacion = asociacionRepository.findByIdWithClubes(id).orElse(null);
        model.addAttribute("asociacion", asociacion);
        // Cargar todos los clubes disponibles
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "asociacion/form";
    }

    @GetMapping
    public String listarAsociaciones(@RequestParam(required = false) String search, Model model) {
        List<Asociacion> asociaciones;
        if (search != null && !search.trim().isEmpty()) {
            asociaciones = asociacionRepository.findByNombreContainingIgnoreCaseOrPaisContainingIgnoreCaseOrPresidenteContainingIgnoreCase(
                search, search, search);
            model.addAttribute("search", search);
        } else {
            asociaciones = asociacionRepository.findAllWithClubes();
        }
        model.addAttribute("asociaciones", asociaciones);
        return "asociacion/list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarAsociacion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Asociacion asociacion = asociacionRepository.findByIdWithClubes(id).orElse(null);
            
            if (asociacion != null) {
                // Verificar si tiene clubes asociados
                if (asociacion.getClubes() != null && !asociacion.getClubes().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", 
                        "No se puede eliminar la asociación '" + asociacion.getNombre() + 
                        "' porque tiene clubes asociados. Primero debe desvincular todos los clubes.");
                    return "redirect:/web/asociaciones";
                }
                
                asociacionRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Asociación eliminada exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Asociación no encontrada!");
            }
            
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar la asociación porque está siendo utilizada por uno o más clubes. " +
                "Primero debe desvincularla de todos los clubes.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la asociación: " + e.getMessage());
        }
        
        return "redirect:/web/asociaciones";
    }
}