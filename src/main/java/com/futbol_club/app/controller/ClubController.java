package com.futbol_club.app.controller;

import com.futbol_club.app.entity.Club;
import com.futbol_club.app.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clubes")
public class ClubController {
    
    @Autowired
    private ClubRepository clubRepository;
    
    @GetMapping
    public List<Club> getAllClubes() {
        return clubRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id) {
        return clubRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Club createClub(@RequestBody Club club) {
        return clubRepository.save(club);
    }
}