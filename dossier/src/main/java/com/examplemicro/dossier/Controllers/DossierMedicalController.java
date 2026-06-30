package com.examplemicro.dossier.Controllers;

import com.examplemicro.dossier.Eentities.*;
import com.examplemicro.dossier.Services.DossierMedicalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.examplemicro.dossier.dto.DossierEnrichiDTO;
import java.util.List;

@RestController
@RequestMapping("/api/dossiers")
public class DossierMedicalController {

    private final DossierMedicalService service;

    public DossierMedicalController(DossierMedicalService service) {
        this.service = service;
    }

    // ─── DOSSIERS ───────────────────────────────────────────────────────────────

    @GetMapping
    public List<DossierMedical> getAllDossiers() {
        return service.getAllDossiers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DossierMedical> getDossierById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getDossierById(id));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<DossierMedical> getDossierByNumero(@PathVariable("numero") String numero) {
        return ResponseEntity.ok(service.getDossierByNumero(numero));
    }

    @GetMapping("/patient/{patientId}")
    public List<DossierMedical> getDossiersByPatient(@PathVariable("patientId") Long patientId) {
        return service.getDossiersByPatient(patientId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<DossierMedical> getDossiersByMedecin(@PathVariable("medecinId") Long medecinId) {
        return service.getDossiersByMedecin(medecinId);
    }

    @PostMapping
    public ResponseEntity<DossierMedical> createDossier(@Valid @RequestBody DossierMedical dossier) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDossier(dossier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DossierMedical> updateDossier(
            @PathVariable("id") Long id, @Valid @RequestBody DossierMedical dossier) {
        return ResponseEntity.ok(service.updateDossier(id, dossier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable("id") Long id) {
        service.deleteDossier(id);
        return ResponseEntity.noContent().build();
    }

    // ─── ORDONNANCES ────────────────────────────────────────────────────────────

    @GetMapping("/{dossierId}/ordonnances")
    public List<Ordonnance> getOrdonnances(@PathVariable("dossierId") Long dossierId) {
        return service.getOrdonnancesByDossier(dossierId);
    }

    @PostMapping("/{dossierId}/ordonnances")
    public ResponseEntity<Ordonnance> addOrdonnance(
            @PathVariable("dossierId") Long dossierId,
            @Valid @RequestBody Ordonnance ordonnance) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addOrdonnance(dossierId, ordonnance));
    }

    @PutMapping("/ordonnances/{id}")
    public ResponseEntity<Ordonnance> updateOrdonnance(
            @PathVariable("id") Long id, @RequestBody Ordonnance ordonnance) {
        return ResponseEntity.ok(service.updateOrdonnance(id, ordonnance));
    }

    @DeleteMapping("/ordonnances/{id}")
    public ResponseEntity<Void> deleteOrdonnance(@PathVariable("id") Long id) {
        service.deleteOrdonnance(id);
        return ResponseEntity.noContent().build();
    }

    // ─── ANALYSES ───────────────────────────────────────────────────────────────

    @GetMapping("/{dossierId}/analyses")
    public List<Analyse> getAnalyses(@PathVariable("dossierId") Long dossierId) {
        return service.getAnalysesByDossier(dossierId);
    }

    @PostMapping("/{dossierId}/analyses")
    public ResponseEntity<Analyse> addAnalyse(
            @PathVariable("dossierId") Long dossierId,
            @Valid @RequestBody Analyse analyse) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addAnalyse(dossierId, analyse));
    }

    @PutMapping("/analyses/{id}")
    public ResponseEntity<Analyse> updateAnalyse(
            @PathVariable("id") Long id, @RequestBody Analyse analyse) {
        return ResponseEntity.ok(service.updateAnalyse(id, analyse));
    }

    @DeleteMapping("/analyses/{id}")
    public ResponseEntity<Void> deleteAnalyse(@PathVariable("id") Long id) {
        service.deleteAnalyse(id);
        return ResponseEntity.noContent().build();
    }

    // ─── TRAITEMENTS ────────────────────────────────────────────────────────────

    @GetMapping("/{dossierId}/traitements")
    public List<Traitement> getTraitements(@PathVariable("dossierId") Long dossierId) {
        return service.getTraitementsByDossier(dossierId);
    }

    @PostMapping("/{dossierId}/traitements")
    public ResponseEntity<Traitement> addTraitement(
            @PathVariable("dossierId") Long dossierId,
            @Valid @RequestBody Traitement traitement) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addTraitement(dossierId, traitement));
    }

    @PutMapping("/traitements/{id}")
    public ResponseEntity<Traitement> updateTraitement(
            @PathVariable("id") Long id, @RequestBody Traitement traitement) {
        return ResponseEntity.ok(service.updateTraitement(id, traitement));
    }

    @DeleteMapping("/traitements/{id}")
    public ResponseEntity<Void> deleteTraitement(@PathVariable("id") Long id) {
        service.deleteTraitement(id);
        return ResponseEntity.noContent().build();
    }
    // Add endpoint
    @GetMapping("/{id}/enrichi")
    public ResponseEntity<DossierEnrichiDTO> getDossierEnrichi(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getDossierEnrichi(id));
    }
}
