package com.examplemicro.dossier.Eentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "analyses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Analyse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    @JsonIgnore
    private DossierMedical dossierMedical;

    @NotBlank(message = "Le type d'analyse est obligatoire")
    @Column(name = "type_analyse")
    private String typeAnalyse;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String resultats;

    @Column(name = "valeurs_normales")
    private String valeursNormales;

    @Column(name = "date_demande")
    private LocalDate dateDemande;

    @Column(name = "date_resultat")
    private LocalDate dateResultat;

    private String laboratoire;

    private String statut = "EN_ATTENTE";

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.dateDemande == null) {
            this.dateDemande = LocalDate.now();
        }
    }
}
