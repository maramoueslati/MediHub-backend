package com.examplemicro.dossier.Exception;

public class DossierNotFoundException extends RuntimeException {
    public DossierNotFoundException(Long id) {
        super("Dossier médical introuvable avec l'id : " + id);
    }
    public DossierNotFoundException(String numero) {
        super("Dossier médical introuvable avec le numéro : " + numero);
    }
}
