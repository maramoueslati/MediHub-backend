package com.examplemicro.rendezvous.dto;

/**
 * DTO envoyé dans la queue RabbitMQ "rdv_notifications"
 * lorsqu'un rendez-vous est créé, confirmé, annulé ou reporté.
 */
public class RdvNotificationDTO {

    private Long rendezVousId;
    private Long patientId;
    private Long medecinId;
    private String type; // RDV_CREE, RDV_CONFIRME, RDV_ANNULE, RDV_REPORTE
    private String dateRendezVous;
    private String heureRendezVous;
    private String motif;

    public RdvNotificationDTO() {}

    public RdvNotificationDTO(Long rendezVousId, Long patientId, Long medecinId,
                               String type, String dateRendezVous,
                               String heureRendezVous, String motif) {
        this.rendezVousId = rendezVousId;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.type = type;
        this.dateRendezVous = dateRendezVous;
        this.heureRendezVous = heureRendezVous;
        this.motif = motif;
    }

    public Long getRendezVousId() { return rendezVousId; }
    public void setRendezVousId(Long rendezVousId) { this.rendezVousId = rendezVousId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getMedecinId() { return medecinId; }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDateRendezVous() { return dateRendezVous; }
    public void setDateRendezVous(String dateRendezVous) { this.dateRendezVous = dateRendezVous; }
    public String getHeureRendezVous() { return heureRendezVous; }
    public void setHeureRendezVous(String heureRendezVous) { this.heureRendezVous = heureRendezVous; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
