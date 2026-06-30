package com.examplemicro.dossier.Services;

import com.examplemicro.dossier.Eentities.*;
import com.examplemicro.dossier.Exception.DossierNotFoundException;
import com.examplemicro.dossier.Repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.examplemicro.dossier.client.UtilisateurClient;
import com.examplemicro.dossier.dto.DossierEnrichiDTO;
import com.examplemicro.dossier.dto.UtilisateurDTO;
import java.util.List;

@Service
@Transactional
public class DossierMedicalService {

    private final DossierMedicalRepository dossierRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final AnalyseRepository analyseRepository;
    private final TraitementRepository traitementRepository;
    private final UtilisateurClient utilisateurClient;
    public DossierMedicalService(
            DossierMedicalRepository dossierRepository,
            OrdonnanceRepository ordonnanceRepository,
            AnalyseRepository analyseRepository,
            TraitementRepository traitementRepository,
            UtilisateurClient utilisateurClient) {
        this.dossierRepository = dossierRepository;
        this.ordonnanceRepository = ordonnanceRepository;
        this.analyseRepository = analyseRepository;
        this.traitementRepository = traitementRepository;
        this.utilisateurClient = utilisateurClient;
    }

    // ─── DOSSIER ────────────────────────────────────────────────────────────────

    public List<DossierMedical> getAllDossiers() {
        return dossierRepository.findAll();
    }

    public DossierMedical getDossierById(Long id) {
        return dossierRepository.findById(id)
                .orElseThrow(() -> new DossierNotFoundException(id));
    }

    public DossierMedical getDossierByNumero(String numero) {
        return dossierRepository.findByNumeroDossier(numero)
                .orElseThrow(() -> new DossierNotFoundException(numero));
    }

    public List<DossierMedical> getDossiersByPatient(Long patientId) {
        return dossierRepository.findByPatientId(patientId);
    }

    public List<DossierMedical> getDossiersByMedecin(Long medecinId) {
        return dossierRepository.findByMedecinId(medecinId);
    }

    public DossierMedical createDossier(DossierMedical dossier) {
        return dossierRepository.save(dossier);
    }

    public DossierMedical updateDossier(Long id, DossierMedical updated) {
        DossierMedical existing = getDossierById(id);
        existing.setPatientId(updated.getPatientId());
        existing.setMedecinId(updated.getMedecinId());
        existing.setRendezVousId(updated.getRendezVousId());
        existing.setGroupeSanguin(updated.getGroupeSanguin());
        existing.setAllergies(updated.getAllergies());
        existing.setAntecedents(updated.getAntecedents());
        existing.setStatut(updated.getStatut());
        return dossierRepository.save(existing);
    }

    public void deleteDossier(Long id) {
        dossierRepository.delete(getDossierById(id));
    }

    // ─── ORDONNANCES ────────────────────────────────────────────────────────────

    public List<Ordonnance> getOrdonnancesByDossier(Long dossierId) {
        return ordonnanceRepository.findByDossierMedicalId(dossierId);
    }

    public Ordonnance addOrdonnance(Long dossierId, Ordonnance ordonnance) {
        DossierMedical dossier = getDossierById(dossierId);
        ordonnance.setDossierMedical(dossier);
        return ordonnanceRepository.save(ordonnance);
    }

    public Ordonnance updateOrdonnance(Long id, Ordonnance updated) {
        Ordonnance existing = ordonnanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordonnance introuvable: " + id));
        existing.setDescription(updated.getDescription());
        existing.setMedicaments(updated.getMedicaments());
        existing.setPosologie(updated.getPosologie());
        existing.setDatePrescription(updated.getDatePrescription());
        existing.setDateExpiration(updated.getDateExpiration());
        existing.setMedecinPrescripteur(updated.getMedecinPrescripteur());
        existing.setStatut(updated.getStatut());
        return ordonnanceRepository.save(existing);
    }

    public void deleteOrdonnance(Long id) {
        ordonnanceRepository.deleteById(id);
    }

    // ─── ANALYSES ───────────────────────────────────────────────────────────────

    public List<Analyse> getAnalysesByDossier(Long dossierId) {
        return analyseRepository.findByDossierMedicalId(dossierId);
    }

    public Analyse addAnalyse(Long dossierId, Analyse analyse) {
        DossierMedical dossier = getDossierById(dossierId);
        analyse.setDossierMedical(dossier);
        return analyseRepository.save(analyse);
    }

    public Analyse updateAnalyse(Long id, Analyse updated) {
        Analyse existing = analyseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Analyse introuvable: " + id));
        existing.setTypeAnalyse(updated.getTypeAnalyse());
        existing.setDescription(updated.getDescription());
        existing.setResultats(updated.getResultats());
        existing.setValeursNormales(updated.getValeursNormales());
        existing.setDateResultat(updated.getDateResultat());
        existing.setLaboratoire(updated.getLaboratoire());
        existing.setStatut(updated.getStatut());
        return analyseRepository.save(existing);
    }

    public void deleteAnalyse(Long id) {
        analyseRepository.deleteById(id);
    }

    // ─── TRAITEMENTS ────────────────────────────────────────────────────────────

    public List<Traitement> getTraitementsByDossier(Long dossierId) {
        return traitementRepository.findByDossierMedicalId(dossierId);
    }

    public Traitement addTraitement(Long dossierId, Traitement traitement) {
        DossierMedical dossier = getDossierById(dossierId);
        traitement.setDossierMedical(dossier);
        return traitementRepository.save(traitement);
    }

    public Traitement updateTraitement(Long id, Traitement updated) {
        Traitement existing = traitementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Traitement introuvable: " + id));
        existing.setNom(updated.getNom());
        existing.setDescription(updated.getDescription());
        existing.setMedicament(updated.getMedicament());
        existing.setPosologie(updated.getPosologie());
        existing.setDuree(updated.getDuree());
        existing.setDateDebut(updated.getDateDebut());
        existing.setDateFin(updated.getDateFin());
        existing.setStatut(updated.getStatut());
        existing.setObservations(updated.getObservations());
        return traitementRepository.save(existing);
    }

    public void deleteTraitement(Long id) {
        traitementRepository.deleteById(id);
    }

public DossierEnrichiDTO getDossierEnrichi(Long id) {
    DossierMedical dossier = getDossierById(id);
    UtilisateurDTO patient = utilisateurClient.getUserById(dossier.getPatientId());
    UtilisateurDTO medecin = dossier.getMedecinId() != null
            ? utilisateurClient.getUserById(dossier.getMedecinId())
            : null;
    return new DossierEnrichiDTO(dossier, patient, medecin);
}}