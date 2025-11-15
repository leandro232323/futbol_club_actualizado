package com.futbol_club.app.repository;

import com.futbol_club.app.entity.Competicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompeticionRepository extends JpaRepository<Competicion, Long> {
    List<Competicion> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT DISTINCT c FROM Competicion c LEFT JOIN FETCH c.clubes")
    List<Competicion> findAllWithClubes();
    
    @Query("SELECT c FROM Competicion c LEFT JOIN FETCH c.clubes WHERE c.id = :id")
    Optional<Competicion> findByIdWithClubes(Long id);
}