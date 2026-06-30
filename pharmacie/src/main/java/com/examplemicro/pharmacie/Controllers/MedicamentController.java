package com.examplemicro.pharmacie.Controllers;

import com.examplemicro.pharmacie.Entities.MedicamentEntity;
import com.examplemicro.pharmacie.Services.MedicamentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentController {

    private final MedicamentService medicamentService;

    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @GetMapping
    public List<MedicamentEntity> getAllMedicaments() {
        return medicamentService.getAllMedicaments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicamentEntity> getMedicamentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(medicamentService.getMedicamentById(id));
    }

    @PostMapping
    public ResponseEntity<MedicamentEntity> createMedicament(
            @Valid @RequestBody MedicamentEntity medicament) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicamentService.createMedicament(medicament));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentEntity> updateMedicament(
            @PathVariable("id") Long id, @Valid @RequestBody MedicamentEntity medicament) {
        return ResponseEntity.ok(medicamentService.updateMedicament(id, medicament));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable("id") Long id) {
        medicamentService.deleteMedicament(id);
        return ResponseEntity.noContent().build();
    }


    /** Entrée/sortie de stock. delta positif = entrée, négatif = sortie */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<MedicamentEntity> ajusterStock(
            @PathVariable("id") Long id, @RequestParam("delta") int delta) {
        return ResponseEntity.ok(medicamentService.ajusterStock(id, delta));
    }

    @GetMapping("/disponibles")
    public List<MedicamentEntity> getMedicamentsDisponibles() {
        return medicamentService.getMedicamentsDisponibles();
    }

    @GetMapping("/rupture-stock")
    public List<MedicamentEntity> getMedicamentsEnRupture() {
        return medicamentService.getMedicamentsEnRupture();
    }

    @GetMapping("/recherche")
    public List<MedicamentEntity> rechercher(@RequestParam String nom) {
        return medicamentService.rechercherParNom(nom);
    }

    /** Utile pour vérifier le load-balancing entre les 2 instances Docker */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of(
                "service", "pharmacie-service",
                "port", medicamentService.getInstancePort(),
                "status", "UP"
        ));
    }
}