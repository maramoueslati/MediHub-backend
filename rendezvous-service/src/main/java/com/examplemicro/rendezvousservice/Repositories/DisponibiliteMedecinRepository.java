package com.examplemicro.rendezvousservice.Repositories;

import com.examplemicro.rendezvousservice.Entities.DisponibiliteMedecin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DisponibiliteMedecinRepository extends JpaRepository<DisponibiliteMedecin, Long> {

    List<DisponibiliteMedecin> findByMedecinId(Long medecinId);

    List<DisponibiliteMedecin> findByMedecinIdAndDisponibleTrue(Long medecinId);

    List<DisponibiliteMedecin> findByMedecinIdAndDateDisponibilite(Long medecinId, LocalDate date);

    List<DisponibiliteMedecin> findByDateDisponibiliteAndDisponibleTrue(LocalDate date);
}
