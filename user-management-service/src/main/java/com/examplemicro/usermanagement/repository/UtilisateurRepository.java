package com.examplemicro.usermanagement.repository;

import com.examplemicro.usermanagement.entity.Role;
import com.examplemicro.usermanagement.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByKeycloakId(String keycloakId);

    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByRole(Role role);

    List<Utilisateur> findByActifTrue();

    boolean existsByEmail(String email);
}
