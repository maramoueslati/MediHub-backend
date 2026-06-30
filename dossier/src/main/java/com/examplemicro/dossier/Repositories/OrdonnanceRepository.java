package com.examplemicro.dossier.Repositories;

import com.examplemicro.dossier.Eentities.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {
    List<Ordonnance> findByDossierMedicalId(Long dossierId);
    List<Ordonnance> findByStatut(String statut);
}
