package com.examplemicro.rendezvousservice.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Historique de toutes les actions effectuées sur un rendez-vous.
 * Traçabilité complète : création, modification, annulation, etc.
 */
@Entity
@Table(name = "historique_rendezvous")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueRendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rendezvous_id", nullable = false)
    private Long rendezVousId;

    @Column(name = "action", nullable = false)
    private String action; // CREATION, MODIFICATION, ANNULATION, CONFIRMATION, etc.

    @Column(name = "ancien_statut")
    private String ancienStatut;

    @Column(name = "nouveau_statut")
    private String nouveauStatut;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "effectue_par")
    private String effectuePar; // PATIENT ou MEDECIN

    @Column(name = "date_action", updatable = false)
    private LocalDateTime dateAction;

    @PrePersist
    protected void onCreate() {
        this.dateAction = LocalDateTime.now();
    }
}
