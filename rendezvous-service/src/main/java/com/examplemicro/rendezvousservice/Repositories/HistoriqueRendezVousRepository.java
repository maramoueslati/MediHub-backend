package com.examplemicro.rendezvousservice.Repositories;

import com.examplemicro.rendezvousservice.Entities.HistoriqueRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueRendezVousRepository extends JpaRepository<HistoriqueRendezVous, Long> {

    List<HistoriqueRendezVous> findByRendezVousIdOrderByDateActionDesc(Long rendezVousId);

    List<HistoriqueRendezVous> findByEffectueParOrderByDateActionDesc(String effectuePar);
}
