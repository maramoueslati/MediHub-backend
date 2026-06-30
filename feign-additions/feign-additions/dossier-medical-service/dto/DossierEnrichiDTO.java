package com.examplemicro.dossier.dto;

import com.examplemicro.dossier.entity.DossierMedical;

/**
 * DTO "enrichi" : combine le DossierMedical local avec les informations
 * du patient/médecin récupérées en temps réel via Feign depuis
 * user-management-service. Utilisé par l'endpoint /api/dossiers/{id}/enrichi
 */
public class DossierEnrichiDTO {
    private DossierMedical dossier;
    private UtilisateurDTO patient;
    private UtilisateurDTO medecin;

    public DossierEnrichiDTO(DossierMedical dossier, UtilisateurDTO patient, UtilisateurDTO medecin) {
        this.dossier = dossier;
        this.patient = patient;
        this.medecin = medecin;
    }

    public DossierMedical getDossier() { return dossier; }
    public void setDossier(DossierMedical dossier) { this.dossier = dossier; }
    public UtilisateurDTO getPatient() { return patient; }
    public void setPatient(UtilisateurDTO patient) { this.patient = patient; }
    public UtilisateurDTO getMedecin() { return medecin; }
    public void setMedecin(UtilisateurDTO medecin) { this.medecin = medecin; }
}
