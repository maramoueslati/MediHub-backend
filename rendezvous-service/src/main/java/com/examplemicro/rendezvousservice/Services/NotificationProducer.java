package com.examplemicro.rendezvousservice.Services;

import com.examplemicro.rendezvousservice.Entities.RendezVous;
import com.examplemicro.rendezvousservice.client.UtilisateurClient;
import com.examplemicro.rendezvousservice.config.RabbitMQConfig;
import com.examplemicro.rendezvousservice.dto.RdvNotificationDTO;
import com.examplemicro.rendezvousservice.dto.UtilisateurDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final UtilisateurClient utilisateurClient;

    public NotificationProducer(RabbitTemplate rabbitTemplate,
                                UtilisateurClient utilisateurClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.utilisateurClient = utilisateurClient;
    }

    public void envoyerNotification(RendezVous rdv, String type) {
        try {
            // Fetch patient email via Feign
            String patientEmail = null;
            try {
                UtilisateurDTO patient = utilisateurClient.getUserById(rdv.getPatientId());
                patientEmail = patient.getEmail();
            } catch (Exception e) {
                System.err.println("⚠️ Impossible de récupérer l'email du patient: " + e.getMessage());
            }

            RdvNotificationDTO dto = new RdvNotificationDTO(
                    rdv.getId(),
                    rdv.getPatientId(),
                    rdv.getMedecinId(),
                    type,
                    rdv.getDateRendezVous() != null ? rdv.getDateRendezVous().toString() : null,
                    rdv.getHeureRendezVous() != null ? rdv.getHeureRendezVous().toString() : null,
                    rdv.getMotif()
            );
            dto.setPatientEmail(patientEmail);

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, dto);
            System.out.println("📤 Notification envoyée → RabbitMQ: [" + type + "] RDV #" + rdv.getId()
                    + (patientEmail != null ? " → " + patientEmail : ""));

        } catch (Exception e) {
            System.err.println("⚠️ Erreur envoi notification: " + e.getMessage());
        }
    }
}