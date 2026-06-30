package com.examplemicro.rendezvousservice.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entité principale représentant un rendez-vous médical.
 * patientId et medecinId sont des références vers d'autres microservices.
 */
@Entity
@Table(name = "rendezvous")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID du patient est obligatoire")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @NotNull(message = "L'ID du médecin est obligatoire")
    @Column(name = "medecin_id", nullable = false)
    private Long medecinId;

    @NotNull(message = "La date est obligatoire")
    @Column(name = "date_rendezvous", nullable = false)
    private LocalDate dateRendezVous;

    @NotNull(message = "L'heure est obligatoire")
    @Column(name = "heure_rendezvous", nullable = false)
    private LocalTime heureRendezVous;

    private String motif;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut = StatutRendezVous.PLANIFIE;

    @Column(name = "createur")
    private String createur; // PATIENT ou MEDECIN

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_mise_a_jour")
    private LocalDateTime dateMiseAJour;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }

    public enum StatutRendezVous {
        PLANIFIE,
        CONFIRME,
        ANNULE,
        TERMINE,
        REPORTE
    }
}
