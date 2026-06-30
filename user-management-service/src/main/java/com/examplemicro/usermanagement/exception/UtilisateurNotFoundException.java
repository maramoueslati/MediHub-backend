package com.examplemicro.usermanagement.exception;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(Long id) {
        super("Utilisateur introuvable avec l'id : " + id);
    }
    public UtilisateurNotFoundException(String identifiant) {
        super("Utilisateur introuvable : " + identifiant);
    }
}
