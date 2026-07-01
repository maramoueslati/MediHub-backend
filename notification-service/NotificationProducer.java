package com.examplemicro.rendezvous.service;

import com.examplemicro.rendezvous.config.RabbitMQConfig;
import com.examplemicro.rendezvous.dto.RdvNotificationDTO;
import com.examplemicro.rendezvous.entity.RendezVous;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Envoie des messages dans la queue RabbitMQ "rdv_notifications"
 * à chaque changement d'état d'un rendez-vous.
 */
@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void envoyerNotification(RendezVous rdv, String type) {
        try {
            RdvNotificationDTO dto = new RdvNotificationDTO(
                rdv.getId(),
                rdv.getPatientId(),
                rdv.getMedecinId(),
                type,
                rdv.getDateRendezVous() != null ? rdv.getDateRendezVous().toString() : null,
                rdv.getHeureRendezVous() != null ? rdv.getHeureRendezVous().toString() : null,
                rdv.getMotif()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, dto);
            System.out.println("📤 Notification envoyée → RabbitMQ: [" + type + "] RDV #" + rdv.getId());

        } catch (Exception e) {
            System.err.println("⚠️ Erreur envoi notification RabbitMQ: " + e.getMessage());
            // Don't throw - notification failure shouldn't break the main operation
        }
    }
}
