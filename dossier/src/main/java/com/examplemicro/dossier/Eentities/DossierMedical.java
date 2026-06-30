package com.examplemicro.dossier.Eentities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité principale du dossier médical.
 * Les références vers Patient, Médecin, et RendezVous
 * sont stockées comme de simples IDs (pas de FK inter-services).
 */
@Entity
@Table(name = "dossiers_medicaux")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID du patient est obligatoire")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "medecin_id")
    private Long medecinId;

    @Column(name = "rendezvous_id")
    private Long rendezVousId;

    @Column(name = "numero_dossier", unique = true)
    private String numeroDossier;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_mise_a_jour")
    private LocalDateTime dateMiseAJour;

    @Column(name = "groupe_sanguin")
    private String groupeSanguin;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String antecedents;

    private String statut = "ACTIF";

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ordonnance> ordonnances = new ArrayList<>();

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analyse> analyses = new ArrayList<>();

    @OneToMany(mappedBy = "dossierMedical", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Traitement> traitements = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
        if (this.numeroDossier == null) {
            this.numeroDossier = "DOS-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }
}
