package com.examplemicro.rendezvousservice.Exception;

public class RendezVousNotFoundException extends RuntimeException {
    public RendezVousNotFoundException(Long id) {
        super("Rendez-vous introuvable avec l'id : " + id);
    }
}
