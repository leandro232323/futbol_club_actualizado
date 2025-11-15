package com.futbol_club.app.repository;

import com.futbol_club.app.entity.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
    
    // Método con JOIN FETCH para cargar relaciones
    @Query("SELECT e FROM Entrenador e LEFT JOIN FETCH e.club")
    List<Entrenador> findAllWithClub();
    
    // Método de búsqueda con relaciones
    @Query("SELECT e FROM Entrenador e LEFT JOIN FETCH e.club WHERE " +
           "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.nacionalidad) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Entrenador> findBySearchTermWithClub(@Param("search") String search);
    
    // Método para encontrar por ID con relaciones
    @Query("SELECT e FROM Entrenador e LEFT JOIN FETCH e.club WHERE e.id = :id")
    Optional<Entrenador> findByIdWithClub(@Param("id") Long id);
    
    // Método original (mantener para compatibilidad)
    List<Entrenador> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrNacionalidadContainingIgnoreCase(
        String nombre, String apellido, String nacionalidad);
}