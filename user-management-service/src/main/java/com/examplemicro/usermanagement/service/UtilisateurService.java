package com.examplemicro.usermanagement.service;

import com.examplemicro.usermanagement.entity.Role;
import com.examplemicro.usermanagement.entity.Utilisateur;
import com.examplemicro.usermanagement.exception.UtilisateurNotFoundException;
import com.examplemicro.usermanagement.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.examplemicro.usermanagement.service.KeycloakAdminService;
import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

@Service
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository repository;
    private final KeycloakAdminService keycloakAdminService;
    public UtilisateurService(UtilisateurRepository repository,
                              KeycloakAdminService keycloakAdminService) {
        this.repository = repository;
        this.keycloakAdminService = keycloakAdminService;
    }


    public Utilisateur getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UtilisateurNotFoundException(id));
    }

    public Utilisateur getByKeycloakId(String keycloakId) {
        return repository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UtilisateurNotFoundException(keycloakId));
    }

    public Utilisateur getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UtilisateurNotFoundException(email));
    }

    public List<Utilisateur> getByRole(Role role) {
        return repository.findByRole(role);
    }

    public List<Utilisateur> getActifs() {
        return repository.findByActifTrue();
    }

    public Utilisateur create(Utilisateur utilisateur, String password) {
        if (repository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        // 1 - Create in Keycloak first
        String keycloakId = keycloakAdminService.createKeycloakUser(utilisateur, password);

        // 2 - Save keycloakId in local profile
        utilisateur.setKeycloakId(keycloakId);

        // 3 - Save in H2
        return repository.save(utilisateur);
    }

    public Utilisateur update(Long id, Utilisateur updated) {
        Utilisateur existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setTelephone(updated.getTelephone());
        existing.setDateNaissance(updated.getDateNaissance());
        existing.setAdresse(updated.getAdresse());
        existing.setSpecialite(updated.getSpecialite());
        existing.setNumeroLicence(updated.getNumeroLicence());
        existing.setActif(updated.isActif());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Utilisateur existing = getById(id);
        // Delete from Keycloak first
        if (existing.getKeycloakId() != null) {
            keycloakAdminService.deleteKeycloakUser(existing.getKeycloakId());
        }
        repository.delete(existing);
    }

    public void desactiver(Long id) {
        Utilisateur u = getById(id);
        u.setActif(false);
        repository.save(u);
    }
    public List<Utilisateur> getAll() {
        // Sync from Keycloak first
        syncFromKeycloak();
        return repository.findAll();
    }

    private void syncFromKeycloak() {
        List<org.keycloak.representations.idm.UserRepresentation> keycloakUsers =
                keycloakAdminService.getAllKeycloakUsers();

        for (org.keycloak.representations.idm.UserRepresentation kUser : keycloakUsers) {
            // Skip if already exists locally
            if (repository.findByKeycloakId(kUser.getId()).isPresent()) continue;
            // Skip system users
            if (kUser.getUsername().equals("admin") || kUser.getEmail() == null) continue;

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
        }
    }
}
