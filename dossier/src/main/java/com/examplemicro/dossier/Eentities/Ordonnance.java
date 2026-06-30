package com.examplemicro.dossier.Eentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordonnances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    @JsonIgnore
    private DossierMedical dossierMedical;

    @NotBlank(message = "La description est obligatoire")
    @Column(columnDefinition = "TEXT")
    private String description;

    private String medicaments;

    private String posologie;

    @Column(name = "date_prescription")
    private LocalDate datePrescription;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "medecin_prescripteur")
    private String medecinPrescripteur;

    private String statut = "ACTIVE";

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.datePrescription == null) {
            this.datePrescription = LocalDate.now();
        }
    }
}
