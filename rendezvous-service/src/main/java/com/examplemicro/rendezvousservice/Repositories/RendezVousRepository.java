package com.examplemicro.rendezvousservice.Repositories;

import com.examplemicro.rendezvousservice.Entities.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    List<RendezVous> findByPatientId(Long patientId);

    List<RendezVous> findByMedecinId(Long medecinId);

    List<RendezVous> findByDateRendezVous(LocalDate date);

    List<RendezVous> findByStatut(RendezVous.StatutRendezVous statut);

    List<RendezVous> findByMedecinIdAndDateRendezVous(Long medecinId, LocalDate date);

    List<RendezVous> findByPatientIdAndStatut(Long patientId, RendezVous.StatutRendezVous statut);

    @Query("SELECT r FROM RendezVous r WHERE r.dateRendezVous BETWEEN :debut AND :fin")
    List<RendezVous> findByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);

    @Query("SELECT r FROM RendezVous r WHERE r.medecinId = :medecinId AND r.dateRendezVous >= :today ORDER BY r.dateRendezVous, r.heureRendezVous")
    List<RendezVous> findProchainsByMedecin(
            @Param("medecinId") Long medecinId,
            @Param("today") LocalDate today);
}
