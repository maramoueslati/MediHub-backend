package com.examplemicro.usermanagement.controller;

import com.examplemicro.usermanagement.entity.Role;
import com.examplemicro.usermanagement.entity.Utilisateur;
import com.examplemicro.usermanagement.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.examplemicro.usermanagement.dto.CreateUtilisateurRequest;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @GetMapping
    public List<Utilisateur> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<Utilisateur> getByKeycloakId(@PathVariable("keycloakId") String keycloakId) {
        return ResponseEntity.ok(service.getByKeycloakId(keycloakId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @GetMapping("/role/{role}")
    public List<Utilisateur> getByRole(@PathVariable("role") Role role) {
        return service.getByRole(role);
    }

    @GetMapping("/actifs")
    public List<Utilisateur> getActifs() {
        return service.getActifs();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Utilisateur> create(@Valid @RequestBody CreateUtilisateurRequest request) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setRole(request.getRole());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setAdresse(request.getAdresse());
        utilisateur.setSpecialite(request.getSpecialite());
        utilisateur.setNumeroLicence(request.getNumeroLicence());
        utilisateur.setActif(true);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(utilisateur, request.getPassword()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> update(@PathVariable("id") Long id, @Valid @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(service.update(id, utilisateur));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactiver")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> desactiver(@PathVariable("id") Long id) {
        service.desactiver(id);
        return ResponseEntity.noContent().build();
    }
}
