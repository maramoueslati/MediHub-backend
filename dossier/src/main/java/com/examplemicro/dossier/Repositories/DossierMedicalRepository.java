package com.examplemicro.dossier.Repositories;

import com.examplemicro.dossier.Eentities.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    List<DossierMedical> findByPatientId(Long patientId);

    List<DossierMedical> findByMedecinId(Long medecinId);

    Optional<DossierMedical> findByNumeroDossier(String numeroDossier);

    List<DossierMedical> findByStatut(String statut);

    boolean existsByPatientId(Long patientId);
}
