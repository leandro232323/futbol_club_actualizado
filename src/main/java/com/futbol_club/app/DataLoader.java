package com.futbol_club.app;

import com.futbol_club.app.entity.*;
import com.futbol_club.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private ClubRepository clubRepository;
    @Autowired private EntrenadorRepository entrenadorRepository;
    @Autowired private JugadorRepository jugadorRepository;
    @Autowired private AsociacionRepository asociacionRepository;
    @Autowired private CompeticionRepository competicionRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // VERIFICAR SI YA HAY DATOS
        if (asociacionRepository.count() > 0 || competicionRepository.count() > 0) {
            System.out.println("⚠️ Los datos ya existen, no se cargan duplicados");
            return;
        }

        // 1. CREAR SOLO 2 ASOCIACIONES (PRIMERO GUARDARLAS)
        Asociacion rfef = new Asociacion();
        rfef.setNombre("Real Federación Española de Fútbol");
        rfef.setPais("España");
        rfef.setPresidente("Pedro Rocha");
        asociacionRepository.save(rfef);

        Asociacion uefa = new Asociacion();
        uefa.setNombre("UEFA");
        uefa.setPais("Europa");
        uefa.setPresidente("Aleksander Čeferin");
        asociacionRepository.save(uefa);

        // 2. CREAR SOLO 2 ENTRENADORES
        Entrenador xavi = new Entrenador();
        xavi.setNombre("Xavi");
        xavi.setApellido("Hernández");
        xavi.setEdad(43);
        xavi.setNacionalidad("Español");
        xavi.setFechaNacimiento(LocalDate.of(1980, 1, 25));
        xavi.setEstiloJuego("Posesión y presión alta");
        xavi.setAnosExperiencia(5);
        entrenadorRepository.save(xavi);

        Entrenador ancelotti = new Entrenador();
        ancelotti.setNombre("Carlo");
        ancelotti.setApellido("Ancelotti");
        ancelotti.setEdad(64);
        ancelotti.setNacionalidad("Italiano");
        ancelotti.setFechaNacimiento(LocalDate.of(1959, 6, 10));
        ancelotti.setEstiloJuego("Fútbol práctico y equilibrado");
        ancelotti.setAnosExperiencia(28);
        entrenadorRepository.save(ancelotti);

        // 3. CREAR SOLO 2 COMPETICIONES (GUARDARLAS INDIVIDUALMENTE)
        Competicion laliga = new Competicion();
        laliga.setNombre("LaLiga EA Sports");
        laliga.setMontoPremio(50000000);
        laliga.setFechaInicio(LocalDate.of(2024, 8, 15));
        laliga.setFechaFin(LocalDate.of(2025, 5, 25));
        competicionRepository.save(laliga);

        Competicion champions = new Competicion();
        champions.setNombre("UEFA Champions League");
        champions.setMontoPremio(80000000);
        champions.setFechaInicio(LocalDate.of(2024, 9, 15));
        champions.setFechaFin(LocalDate.of(2025, 6, 10));
        competicionRepository.save(champions);

        // 4. CREAR SOLO 2 JUGADORES
        Jugador lewandowski = new Jugador();
        lewandowski.setNombre("Robert");
        lewandowski.setApellido("Lewandowski");
        lewandowski.setNumero(9);
        lewandowski.setPosicion("Delantero");
        lewandowski.setFechaNacimiento(LocalDate.of(1988, 8, 21));
        lewandowski.setNacionalidad("Polaco");
        lewandowski.setValorMercado(30.0);
        jugadorRepository.save(lewandowski);

        Jugador pedri = new Jugador();
        pedri.setNombre("Pedro");
        pedri.setApellido("González");
        pedri.setNumero(8);
        pedri.setPosicion("Centrocampista");
        pedri.setFechaNacimiento(LocalDate.of(2002, 11, 25));
        pedri.setNacionalidad("Español");
        pedri.setValorMercado(100.0);
        jugadorRepository.save(pedri);

        // 5. CREAR SOLO 2 CLUBES (SIN ASIGNAR COMPETICIONES NI ASOCIACIONES EN RELACIONES MANYTOMANY)
        Club barcelona = new Club();
        barcelona.setNombre("FC Barcelona");
        barcelona.setFechaFundacion(LocalDate.of(1899, 11, 29));
        barcelona.setEstadio("Spotify Camp Nou");
        barcelona.setCiudad("Barcelona");
        barcelona.setPresidente("Joan Laporta");
        barcelona.setEntrenador(xavi);
        // NO asignar jugadores, asociaciones o competiciones si son relaciones ManyToMany
        clubRepository.save(barcelona);

        Club realMadrid = new Club();
        realMadrid.setNombre("Real Madrid CF");
        realMadrid.setFechaFundacion(LocalDate.of(1902, 3, 6));
        realMadrid.setEstadio("Santiago Bernabéu");
        realMadrid.setCiudad("Madrid");
        realMadrid.setPresidente("Florentino Pérez");
        realMadrid.setEntrenador(ancelotti);
        clubRepository.save(realMadrid);

        System.out.println("✅ DATOS CARGADOS EXITOSAMENTE!");
        System.out.println("🏆 Asociaciones: " + asociacionRepository.count());
        System.out.println("👨‍🏫 Entrenadores: " + entrenadorRepository.count());
        System.out.println("🎯 Competiciones: " + competicionRepository.count());
        System.out.println("👥 Jugadores: " + jugadorRepository.count());
        System.out.println("🏟️ Clubes: " + clubRepository.count());
    }
}