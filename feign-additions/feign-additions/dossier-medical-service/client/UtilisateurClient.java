package com.examplemicro.dossier.client;

import com.examplemicro.dossier.dto.UtilisateurDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management-service")
public interface UtilisateurClient {

    @GetMapping("/api/users/{id}")
    UtilisateurDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/keycloak/{keycloakId}")
    UtilisateurDTO getUserByKeycloakId(@PathVariable("keycloakId") String keycloakId);
}
