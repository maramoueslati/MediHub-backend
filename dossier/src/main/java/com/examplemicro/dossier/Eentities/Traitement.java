package com.examplemicro.dossier.Eentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "traitements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    @JsonIgnore
    private DossierMedical dossierMedical;

    @NotBlank(message = "Le nom du traitement est obligatoire")
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String medicament;

    private String posologie;

    private String duree;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    private String statut = "EN_COURS";

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.dateDebut == null) {
            this.dateDebut = LocalDate.now();
        }
    }
}
