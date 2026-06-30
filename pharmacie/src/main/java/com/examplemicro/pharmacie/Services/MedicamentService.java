package com.examplemicro.pharmacie.Services;

import com.examplemicro.pharmacie.Entities.MedicamentEntity;
import com.examplemicro.pharmacie.Exception.MedicamentNotFoundException;
import com.examplemicro.pharmacie.Repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;

    @Value("${server.port}")
    private String instancePort;

    public MedicamentService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    public List<MedicamentEntity> getAllMedicaments() {
        return medicamentRepository.findAll();
    }

    public MedicamentEntity getMedicamentById(Long id) {
        return medicamentRepository.findById(id)
                .orElseThrow(() -> new MedicamentNotFoundException(id));
    }

    public MedicamentEntity createMedicament(MedicamentEntity medicament) {
        return medicamentRepository.save(medicament);
    }

    public MedicamentEntity updateMedicament(Long id, MedicamentEntity updated) {
        MedicamentEntity existing = getMedicamentById(id);
        existing.setNom(updated.getNom());
        existing.setDescription(updated.getDescription());
        existing.setCategorie(updated.getCategorie());
        existing.setFabricant(updated.getFabricant());
        existing.setPrix(updated.getPrix());
        existing.setQuantiteStock(updated.getQuantiteStock());
        existing.setSeuilAlerte(updated.getSeuilAlerte());
        existing.setDateExpiration(updated.getDateExpiration());
        return medicamentRepository.save(existing);
    }

    public void deleteMedicament(Long id) {
        medicamentRepository.delete(getMedicamentById(id));
    }

    public MedicamentEntity ajusterStock(Long id, int delta) {
        MedicamentEntity medicament = getMedicamentById(id);
        int nouvelleQuantite = medicament.getQuantiteStock() + delta;
        if (nouvelleQuantite < 0) {
            throw new IllegalArgumentException(
                    "Stock insuffisant pour : " + medicament.getNom());
        }
        medicament.setQuantiteStock(nouvelleQuantite);
        return medicamentRepository.save(medicament);
    }

    public List<MedicamentEntity> getMedicamentsDisponibles() {
        return medicamentRepository.findByQuantiteStockGreaterThan(0);
    }

    public List<MedicamentEntity> getMedicamentsEnRupture() {
        return medicamentRepository.findMedicamentsEnRupture();
    }

    public List<MedicamentEntity> rechercherParNom(String nom) {
        return medicamentRepository.findByNomContainingIgnoreCase(nom);
    }

    public String getInstancePort() {
        return instancePort;
    }
}