package com.examplemicro.rendezvousservice.Controllers;

import com.examplemicro.rendezvousservice.Entities.DisponibiliteMedecin;
import com.examplemicro.rendezvousservice.Entities.HistoriqueRendezVous;
import com.examplemicro.rendezvousservice.Entities.RendezVous;
import com.examplemicro.rendezvousservice.Services.RendezVousService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import com.examplemicro.rendezvousservice.dto.RendezVousEnrichiDTO;
@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    private final RendezVousService service;

    public RendezVousController(RendezVousService service) {
        this.service = service;
    }

    // ─── RENDEZ-VOUS ────────────────────────────────────────────────────────────

    @GetMapping
    public List<RendezVous> getAllRendezVous() {
        return service.getAllRendezVous();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getRendezVousById(id));
    }

    @GetMapping("/patient/{patientId}")
    public List<RendezVous> getByPatient(@PathVariable("patientId") Long patientId) {
        return service.getRendezVousByPatient(patientId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<RendezVous> getByMedecin(@PathVariable("medecinId") Long medecinId) {
        return service.getRendezVousByMedecin(medecinId);
    }

    @GetMapping("/medecin/{medecinId}/prochains")
    public List<RendezVous> getProchainsByMedecin(@PathVariable("medecinId") Long medecinId) {
        return service.getProchainsByMedecin(medecinId);
    }

    @GetMapping("/date/{date}")
    public List<RendezVous> getByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getRendezVousByDate(date);
    }

    @GetMapping("/statut/{statut}")
    public List<RendezVous> getByStatut(@PathVariable("statut") RendezVous.StatutRendezVous statut) {
        return service.getRendezVousByStatut(statut);
    }

    @PostMapping
    public ResponseEntity<RendezVous> create(@Valid @RequestBody RendezVous rendezVous) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRendezVous(rendezVous));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> update(
            @PathVariable("id") Long id, @Valid @RequestBody RendezVous rendezVous) {
        return ResponseEntity.ok(service.updateRendezVous(id, rendezVous));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<RendezVous> changerStatut(
            @PathVariable("id") Long id,
            @RequestParam("statut") RendezVous.StatutRendezVous statut,
            @RequestParam(value = "acteur", defaultValue = "SYSTEM") String acteur) {
        return ResponseEntity.ok(service.changerStatut(id, statut, acteur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    // ─── DISPONIBILITES ─────────────────────────────────────────────────────────

    @GetMapping("/disponibilites/medecin/{medecinId}")
    public List<DisponibiliteMedecin> getDisponibilitesByMedecin(
            @PathVariable("medecinId") Long medecinId) {
        return service.getDisponibilitesByMedecin(medecinId);
    }

    @GetMapping("/disponibilites/medecin/{medecinId}/date/{date}")
    public List<DisponibiliteMedecin> getDisponibilitesByMedecinAndDate(
            @PathVariable("medecinId") Long medecinId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getDisponibilitesByMedecinAndDate(medecinId, date);
    }

    @GetMapping("/disponibilites/date/{date}")
    public List<DisponibiliteMedecin> getDisponiblesParDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getDisponiblesParDate(date);
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteMedecin> addDisponibilite(
            @Valid @RequestBody DisponibiliteMedecin disponibilite) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addDisponibilite(disponibilite));
    }

    @PutMapping("/disponibilites/{id}")
    public ResponseEntity<DisponibiliteMedecin> updateDisponibilite(
            @PathVariable("id") Long id,
            @RequestBody DisponibiliteMedecin disponibilite) {
        return ResponseEntity.ok(service.updateDisponibilite(id, disponibilite));
    }

    @DeleteMapping("/disponibilites/{id}")
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable("id") Long id) {
        service.deleteDisponibilite(id);
        return ResponseEntity.noContent().build();
    }

    // ─── HISTORIQUE ─────────────────────────────────────────────────────────────

    @GetMapping("/{id}/historique")
    public List<HistoriqueRendezVous> getHistorique(@PathVariable("id") Long id) {
        return service.getHistoriqueByRendezVous(id);
    }
    @GetMapping("/{id}/enrichi")
    public ResponseEntity<RendezVousEnrichiDTO> getRendezVousEnrichi(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getRendezVousEnrichi(id));
    }
}
