package com.examplemicro.usermanagement.service;

import com.examplemicro.usermanagement.entity.Role;
import com.examplemicro.usermanagement.entity.Utilisateur;
import com.examplemicro.usermanagement.repository.UtilisateurRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Synchronise automatiquement les utilisateurs Keycloak vers H2
 * au démarrage de l'application.
 */
@Service
public class StartupSyncService {

    private final KeycloakAdminService keycloakAdminService;
    private final UtilisateurRepository repository;

    public StartupSyncService(KeycloakAdminService keycloakAdminService,
                              UtilisateurRepository repository) {
        this.keycloakAdminService = keycloakAdminService;
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncUsersOnStartup() {
        System.out.println("🔄 Synchronisation des utilisateurs Keycloak → H2 au démarrage...");
        try {
            List<UserRepresentation> keycloakUsers = keycloakAdminService.getAllKeycloakUsers();

            int count = 0;
            for (UserRepresentation kUser : keycloakUsers) {
                // Skip admin and users without email
                if (kUser.getEmail() == null) continue;
                if ("admin".equals(kUser.getUsername())) continue;

                // Skip if already exists in H2
                if (repository.findByKeycloakId(kUser.getId()).isPresent()) continue;
                if (repository.findByEmail(kUser.getEmail()).isPresent()) continue;

                Utilisateur u = new Utilisateur();
                u.setKeycloakId(kUser.getId());
                u.setNom(kUser.getLastName() != null ? kUser.getLastName() : kUser.getUsername());
                u.setPrenom(kUser.getFirstName() != null ? kUser.getFirstName() : "");
                u.setEmail(kUser.getEmail());
                u.setActif(Boolean.TRUE.equals(kUser.isEnabled()));

                // Get role from Keycloak
                Role role = keycloakAdminService.getUserRole(kUser.getId());
                u.setRole(role != null ? role : Role.PATIENT);

                repository.save(u);
                count++;
            }

            System.out.println("✅ " + count + " utilisateur(s) synchronisé(s) depuis Keycloak.");
        } catch (Exception e) {
            System.err.println("⚠️ Erreur lors de la synchronisation Keycloak: " + e.getMessage());
        }
    }
}