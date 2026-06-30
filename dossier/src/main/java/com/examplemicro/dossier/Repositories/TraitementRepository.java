package com.examplemicro.dossier.Repositories;

import com.examplemicro.dossier.Eentities.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraitementRepository extends JpaRepository<Traitement, Long> {
    List<Traitement> findByDossierMedicalId(Long dossierId);
    List<Traitement> findByStatut(String statut);
}
