package com.examplemicro.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Profil utilisateur MediHub.
 * L'authentification réelle (mot de passe, token) est gérée par Keycloak.
 * Cette entité stocke uniquement les données métier/profil, liées à
 * l'utilisateur Keycloak via "keycloakId" (le champ "sub" du token JWT).
 */
@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identifiant Keycloak (claim "sub" du token JWT). Unique. */
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Column(unique = true)
    private String email;

    private String telephone;

    @NotNull(message = "Le rôle est obligatoire")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    private String adresse;

    /** Spécialité, uniquement pertinent si role = MEDECIN */
    private String specialite;

    /** Numéro de licence, pertinent pour MEDECIN ou PHARMACIEN */
    @Column(name = "numero_licence")
    private String numeroLicence;

    private boolean actif = true;

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
}
