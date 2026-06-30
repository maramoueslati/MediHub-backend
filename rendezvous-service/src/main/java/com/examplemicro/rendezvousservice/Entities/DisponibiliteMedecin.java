package com.examplemicro.rendezvousservice.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Représente un créneau de disponibilité d'un médecin.
 * Permet de vérifier si un médecin est disponible avant de créer un RDV.
 */
@Entity
@Table(name = "disponibilites_medecin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisponibiliteMedecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'ID du médecin est obligatoire")
    @Column(name = "medecin_id", nullable = false)
    private Long medecinId;

    @NotNull(message = "La date est obligatoire")
    @Column(name = "date_disponibilite", nullable = false)
    private LocalDate dateDisponibilite;

    @NotNull(message = "L'heure de début est obligatoire")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    private boolean disponible = true;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
    }
}
