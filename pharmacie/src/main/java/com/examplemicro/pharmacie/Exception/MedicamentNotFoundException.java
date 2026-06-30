package com.examplemicro.pharmacie.Exception;


public class MedicamentNotFoundException extends RuntimeException {
    public MedicamentNotFoundException(Long id) {
        super("Médicament introuvable avec l'id : " + id);
    }
}