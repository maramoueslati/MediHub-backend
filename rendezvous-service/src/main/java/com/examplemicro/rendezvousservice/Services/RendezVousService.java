package com.examplemicro.rendezvousservice.Services;

import com.examplemicro.rendezvousservice.Entities.DisponibiliteMedecin;
import com.examplemicro.rendezvousservice.Entities.HistoriqueRendezVous;
import com.examplemicro.rendezvousservice.Entities.RendezVous;
import com.examplemicro.rendezvousservice.Exception.RendezVousNotFoundException;
import com.examplemicro.rendezvousservice.Repositories.DisponibiliteMedecinRepository;
import com.examplemicro.rendezvousservice.Repositories.HistoriqueRendezVousRepository;
import com.examplemicro.rendezvousservice.Repositories.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import com.examplemicro.rendezvousservice.dto.RendezVousEnrichiDTO;
import com.examplemicro.rendezvousservice.dto.UtilisateurDTO;
import com.examplemicro.rendezvousservice.client.UtilisateurClient;
@Service
@Transactional
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final DisponibiliteMedecinRepository disponibiliteRepository;
    private final HistoriqueRendezVousRepository historiqueRepository;
    private final UtilisateurClient utilisateurClient;
    private final NotificationProducer notificationProducer;

    public RendezVousService(
            RendezVousRepository rendezVousRepository,
            DisponibiliteMedecinRepository disponibiliteRepository,
            HistoriqueRendezVousRepository historiqueRepository,
            UtilisateurClient utilisateurClient,
            NotificationProducer notificationProducer) {    // ← ADD THIS
        this.rendezVousRepository = rendezVousRepository;
        this.disponibiliteRepository = disponibiliteRepository;
        this.historiqueRepository = historiqueRepository;
        this.utilisateurClient = utilisateurClient;
        this.notificationProducer = notificationProducer;   // ← ADD THIS
    }
    // ─── RENDEZ-VOUS ────────────────────────────────────────────────────────────

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RendezVousNotFoundException(id));
    }

    public List<RendezVous> getRendezVousByPatient(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }

    public List<RendezVous> getRendezVousByMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    public List<RendezVous> getRendezVousByDate(LocalDate date) {
        return rendezVousRepository.findByDateRendezVous(date);
    }

    public List<RendezVous> getRendezVousByStatut(RendezVous.StatutRendezVous statut) {
        return rendezVousRepository.findByStatut(statut);
    }

    public List<RendezVous> getProchainsByMedecin(Long medecinId) {
        return rendezVousRepository.findProchainsByMedecin(medecinId, LocalDate.now());
    }

    public RendezVous createRendezVous(RendezVous rendezVous) {
        RendezVous saved = rendezVousRepository.save(rendezVous);
        enregistrerHistorique(saved.getId(), "CREATION", null,
                saved.getStatut().name(), "Rendez-vous créé", saved.getCreateur());
        notificationProducer.envoyerNotification(saved, "RDV_CREE");
        return saved;
    }

    public RendezVous updateRendezVous(Long id, RendezVous updated) {
        RendezVous existing = getRendezVousById(id);
        String ancienStatut = existing.getStatut().name();

        existing.setPatientId(updated.getPatientId());
        existing.setMedecinId(updated.getMedecinId());
        existing.setDateRendezVous(updated.getDateRendezVous());
        existing.setHeureRendezVous(updated.getHeureRendezVous());
        existing.setMotif(updated.getMotif());
        existing.setNotes(updated.getNotes());
        existing.setStatut(updated.getStatut());

        RendezVous saved = rendezVousRepository.save(existing);
        enregistrerHistorique(id, "MODIFICATION", ancienStatut,
                saved.getStatut().name(), "Rendez-vous modifié", updated.getCreateur());
         return saved;
    }

    public RendezVous changerStatut(Long id, RendezVous.StatutRendezVous nouveauStatut, String acteur) {
        RendezVous existing = getRendezVousById(id);
        String ancienStatut = existing.getStatut().name();
        existing.setStatut(nouveauStatut);
        RendezVous saved = rendezVousRepository.save(existing);

        // historique
        enregistrerHistorique(id, "CHANGEMENT_STATUT", ancienStatut,
                nouveauStatut.name(), "Statut modifié par " + acteur, acteur);

        // notification RabbitMQ
        notificationProducer.envoyerNotification(saved, "RDV_" + nouveauStatut.name());

        return saved;
    }

    public void deleteRendezVous(Long id) {
        RendezVous existing = getRendezVousById(id);
        enregistrerHistorique(id, "SUPPRESSION", existing.getStatut().name(),
                null, "Rendez-vous supprimé", "SYSTEM");
        rendezVousRepository.delete(existing);
    }

    // ─── DISPONIBILITES ─────────────────────────────────────────────────────────

    public List<DisponibiliteMedecin> getDisponibilitesByMedecin(Long medecinId) {
        return disponibiliteRepository.findByMedecinId(medecinId);
    }

    public List<DisponibiliteMedecin> getDisponibilitesByMedecinAndDate(Long medecinId, LocalDate date) {
        return disponibiliteRepository.findByMedecinIdAndDateDisponibilite(medecinId, date);
    }

    public List<DisponibiliteMedecin> getDisponiblesParDate(LocalDate date) {
        return disponibiliteRepository.findByDateDisponibiliteAndDisponibleTrue(date);
    }

    public DisponibiliteMedecin addDisponibilite(DisponibiliteMedecin disponibilite) {
        return disponibiliteRepository.save(disponibilite);
    }

    public DisponibiliteMedecin updateDisponibilite(Long id, DisponibiliteMedecin updated) {
        DisponibiliteMedecin existing = disponibiliteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilité introuvable: " + id));
        existing.setDateDisponibilite(updated.getDateDisponibilite());
        existing.setHeureDebut(updated.getHeureDebut());
        existing.setHeureFin(updated.getHeureFin());
        existing.setDisponible(updated.isDisponible());
        return disponibiliteRepository.save(existing);
    }

    public void deleteDisponibilite(Long id) {
        disponibiliteRepository.deleteById(id);
    }

    // ─── HISTORIQUE ─────────────────────────────────────────────────────────────

    public List<HistoriqueRendezVous> getHistoriqueByRendezVous(Long rendezVousId) {
        return historiqueRepository.findByRendezVousIdOrderByDateActionDesc(rendezVousId);
    }

    private void enregistrerHistorique(Long rendezVousId, String action,
                                       String ancienStatut, String nouveauStatut,
                                       String details, String effectuePar) {
        HistoriqueRendezVous historique = new HistoriqueRendezVous();
        historique.setRendezVousId(rendezVousId);
        historique.setAction(action);
        historique.setAncienStatut(ancienStatut);
        historique.setNouveauStatut(nouveauStatut);
        historique.setDetails(details);
        historique.setEffectuePar(effectuePar);
        historiqueRepository.save(historique);
    }
    public RendezVousEnrichiDTO getRendezVousEnrichi(Long id) {
        RendezVous rdv = getRendezVousById(id);
        UtilisateurDTO patient = utilisateurClient.getUserById(rdv.getPatientId());
        UtilisateurDTO medecin = utilisateurClient.getUserById(rdv.getMedecinId());
        return new RendezVousEnrichiDTO(rdv, patient, medecin);
    }
}
