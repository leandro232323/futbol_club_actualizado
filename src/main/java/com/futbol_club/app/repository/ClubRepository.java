package com.futbol_club.app.repository;

import com.futbol_club.app.entity.Club;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByNombre(String nombre);
    
    @Query("SELECT c FROM Club c JOIN c.competiciones comp WHERE comp.id = :competicionId")
    List<Club> findByCompeticionesId(@Param("competicionId") Long competicionId);

    List<Club> findByNombreContainingIgnoreCaseOrEstadioContainingIgnoreCaseOrCiudadContainingIgnoreCase(
        String nombre, String estadio, String ciudad);
        
    List<Club> findByAsociacionId(Long asociacionId);
}