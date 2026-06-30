    package com.examplemicro.dossier.Repositories;

import com.examplemicro.dossier.Eentities.Analyse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalyseRepository extends JpaRepository<Analyse, Long> {
    List<Analyse> findByDossierMedicalId(Long dossierId);
    List<Analyse> findByStatut(String statut);
    List<Analyse> findByTypeAnalyse(String typeAnalyse);
}
