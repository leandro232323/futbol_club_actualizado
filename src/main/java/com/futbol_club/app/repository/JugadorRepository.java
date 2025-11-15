package com.futbol_club.app.repository;

import com.futbol_club.app.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    // Método con JOIN FETCH para cargar relaciones
    @Query("SELECT j FROM Jugador j LEFT JOIN FETCH j.club")
    List<Jugador> findAllWithClub();
    
    // Método de búsqueda con relaciones
    @Query("SELECT j FROM Jugador j LEFT JOIN FETCH j.club WHERE " +
           "LOWER(j.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(j.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(j.posicion) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Jugador> findBySearchTermWithClub(@Param("search") String search);
    
    // Método para encontrar por ID con relaciones
    @Query("SELECT j FROM Jugador j LEFT JOIN FETCH j.club WHERE j.id = :id")
    Optional<Jugador> findByIdWithClub(@Param("id") Long id);
    
    // Métodos adicionales útiles
    List<Jugador> findByClubId(Long clubId);
    List<Jugador> findByPosicion(String posicion);
    
    // Método original (mantener para compatibilidad)
    List<Jugador> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrPosicionContainingIgnoreCase(
        String nombre, String apellido, String posicion);
}