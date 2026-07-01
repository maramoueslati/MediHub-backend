package com.examplemicro.rendezvousservice.dto;

import com.examplemicro.rendezvousservice.Entities.RendezVous;

public class RendezVousEnrichiDTO {
    private RendezVous rendezVous;
    private UtilisateurDTO patient;
    private UtilisateurDTO medecin;

    public RendezVousEnrichiDTO(RendezVous rendezVous,
                                UtilisateurDTO patient,
                                UtilisateurDTO medecin) {
        this.rendezVous = rendezVous;
        this.patient = patient;
        this.medecin = medecin;
    }

    public RendezVous getRendezVous() { return rendezVous; }
    public void setRendezVous(RendezVous rendezVous) { this.rendezVous = rendezVous; }
    public UtilisateurDTO getPatient() { return patient; }
    public void setPatient(UtilisateurDTO patient) { this.patient = patient; }
    public UtilisateurDTO getMedecin() { return medecin; }
    public void setMedecin(UtilisateurDTO medecin) { this.medecin = medecin; }
}