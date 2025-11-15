package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Club;
import com.futbol_club.app.entity.Jugador;
import com.futbol_club.app.repository.ClubRepository;
import com.futbol_club.app.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/web/jugadores")
public class JugadorWebController {
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private ClubRepository clubRepository;
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("jugador", new Jugador());
        // Cargar todos los clubes disponibles para asociar
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "jugador/form";
    }
    
    @PostMapping("/guardar")
    public String guardarJugador(@ModelAttribute Jugador jugador, 
                                @RequestParam(value = "clubId", required = false) Long clubId,
                                RedirectAttributes redirectAttributes) {
        try {
            // Asignar club si se seleccionó
            if (clubId != null) {
                Club club = clubRepository.findById(clubId).orElse(null);
                jugador.setClub(club);
            }
            
            jugadorRepository.save(jugador);
            redirectAttributes.addFlashAttribute("success", "Jugador guardado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el jugador: " + e.getMessage());
        }
        return "redirect:/web/jugadores";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Jugador jugador = jugadorRepository.findByIdWithClub(id).orElse(null);
        model.addAttribute("jugador", jugador);
        // Cargar todos los clubes disponibles
        model.addAttribute("todosLosClubes", clubRepository.findAll());
        return "jugador/form";
    }

    @GetMapping
    public String listarJugadores(@RequestParam(required = false) String search, Model model) {
        List<Jugador> jugadores;
        
        if (search != null && !search.trim().isEmpty()) {
            // Usar el nuevo método con JOIN FETCH
            jugadores = jugadorRepository.findBySearchTermWithClub(search);
            model.addAttribute("search", search);
        } else {
            // Usar el nuevo método con JOIN FETCH
            jugadores = jugadorRepository.findAllWithClub();
        }
        model.addAttribute("jugadores", jugadores);
        return "jugador/list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Jugador jugador = jugadorRepository.findByIdWithClub(id).orElse(null);
            
            if (jugador != null) {
                jugadorRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Jugador eliminado exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Jugador no encontrado!");
            }
            
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", 
                "No se puede eliminar el jugador porque está asociado a un club. " +
                "Primero debe desvincularlo del club.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el jugador: " + e.getMessage());
        }
        
        return "redirect:/web/jugadores";
    }
}