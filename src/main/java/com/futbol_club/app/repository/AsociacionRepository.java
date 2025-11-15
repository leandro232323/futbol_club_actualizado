package com.futbol_club.app.repository;

import com.futbol_club.app.entity.Asociacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsociacionRepository extends JpaRepository<Asociacion, Long> {
    List<Asociacion> findByNombreContainingIgnoreCaseOrPaisContainingIgnoreCaseOrPresidenteContainingIgnoreCase(
        String nombre, String pais, String presidente);
    
    @Query("SELECT DISTINCT a FROM Asociacion a LEFT JOIN FETCH a.clubes")
    List<Asociacion> findAllWithClubes();
    
    @Query("SELECT a FROM Asociacion a LEFT JOIN FETCH a.clubes WHERE a.id = :id")
    Optional<Asociacion> findByIdWithClubes(Long id);
}