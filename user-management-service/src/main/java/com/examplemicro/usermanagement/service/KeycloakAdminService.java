package com.examplemicro.usermanagement.service;

import com.examplemicro.usermanagement.entity.Role;
import com.examplemicro.usermanagement.entity.Utilisateur;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    @Value("${keycloak.admin.server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    /**
     * Creates a user in Keycloak and returns the Keycloak user ID
     */
    public String createKeycloakUser(Utilisateur utilisateur, String password) {
        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Build user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(utilisateur.getEmail());
        user.setEmail(utilisateur.getEmail());
        user.setFirstName(utilisateur.getPrenom());
        user.setLastName(utilisateur.getNom());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Create user in Keycloak
        Response response = usersResource.create(user);

        if (response.getStatus() != 201) {
            throw new IllegalArgumentException(
                    "Erreur lors de la création dans Keycloak: " + response.getStatus());
        }

        // Extract the new user's ID from the response Location header
        String locationHeader = response.getHeaderString("Location");
        String keycloakUserId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

        // Set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        usersResource.get(keycloakUserId).resetPassword(credential);

        // Assign realm role
        RoleRepresentation role = realmResource.roles()
                .get(utilisateur.getRole().name())
                .toRepresentation();
        usersResource.get(keycloakUserId).roles().realmLevel()
                .add(Collections.singletonList(role));

        keycloak.close();
        return keycloakUserId;
    }

    /**
     * Deletes a user from Keycloak by keycloakId
     */
    public void deleteKeycloakUser(String keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        keycloak.realm(realm).users().delete(keycloakId);
        keycloak.close();
    }

    /**
     * Sync: load all existing Keycloak users into local DB representation
     */
    public List<UserRepresentation> getAllKeycloakUsers() {
        Keycloak keycloak = getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().list();
        keycloak.close();
        return users;
    }
    public Role getUserRole(String keycloakUserId) {
        Keycloak keycloak = getKeycloakInstance();
        try {
            List<RoleRepresentation> roles = keycloak.realm(realm)
                    .users()
                    .get(keycloakUserId)
                    .roles()
                    .realmLevel()
                    .listEffective();

            for (RoleRepresentation role : roles) {
                try {
                    return com.examplemicro.usermanagement.entity.Role
                            .valueOf(role.getName().toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    // skip system roles like offline_access, uma_authorization
                }
            }
        } finally {
            keycloak.close();
        }
        return null;
    }
}